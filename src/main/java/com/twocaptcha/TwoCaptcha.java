package com.twocaptcha;

import com.twocaptcha.captcha.Captcha;
import com.twocaptcha.captcha.ReCaptcha;
import com.twocaptcha.exceptions.ApiException;
import com.twocaptcha.exceptions.NetworkException;
import com.twocaptcha.exceptions.TimeoutException;
import com.twocaptcha.exceptions.ValidationException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Class TwoCaptcha
 */
public class TwoCaptcha {

    /**
     * API KEY
     */
    private String apiKey;

    /**
     * ID of software developer. Developers who integrated their software
     * with our service get reward: 10% of spendings of their software users.
     */
    private int softId;

    /**
     * URL to which the result will be sent
     */
    private String callback;

    /**
     * How long should wait for captcha result (in seconds)
     */
    private int defaultTimeout = 120;

    /**
     * How long should wait for recaptcha result (in seconds)
     */
    private int recaptchaTimeout = 600;

    /**
     * How often do requests to `/res.php` should be made
     * in order to check if a result is ready (in seconds)
     */
    private int pollingInterval = 10;

    /**
     * Helps to understand if there is need of waiting
     * for result or not (because callback was used)
     */
    private boolean lastCaptchaHasCallback;

    /**
     * Network client
     */
    private ApiClient apiClient;

    /**
     * TwoCaptcha constructor
     */
    public TwoCaptcha() {
        this.apiClient = new ApiClient();
    }

    /**
     * TwoCaptcha constructor
     *
     * @param apiKey api key for 2captcha
     */
    public TwoCaptcha(String apiKey) {
        this();
        setApiKey(apiKey);
    }

    /**
     * @param apiKey api key for 2captcha
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * @param softId developer software id for 2captcha
     */
    public void setSoftId(int softId) {
        this.softId = softId;
    }

    /**
     * @param callback callback url
     */
    public void setCallback(String callback) {
        this.callback = callback;
    }

    /**
     * @param timeout timeout for captcha
     */
    public void setDefaultTimeout(int timeout) {
        this.defaultTimeout = timeout;
    }

    /**
     * @param timeout timeout for recaptcha
     */
    public void setRecaptchaTimeout(int timeout) {
        this.recaptchaTimeout = timeout;
    }

    /**
     * @param interval interval between checks
     */
    public void setPollingInterval(int interval) {
        this.pollingInterval = interval;
    }

    /**
     * @param apiClient api client
     */
    public void setHttpClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Sends captcha to `/in.php` and waits for it's result.
     * This helper can be used instead of manual using of `send` and `getResult` functions.
     *
     * @param captcha captcha
     * @throws Exception unable to do something
     */
    public void solve(Captcha captcha) throws Exception {
        Map<String, Integer> waitOptions = new HashMap<>();

        if (captcha instanceof ReCaptcha) {
            waitOptions.put("timeout", recaptchaTimeout);
        }

        solve(captcha, waitOptions);
    }

    /**
     * Sends captcha to `/in.php` and waits for it's result.
     * This helper can be used instead of manual using of `send` and `getResult` functions.
     *
     * @param captcha captcha to solve
     * @param waitOptions wait
     * @throws Exception unable to do something
     */
    public void solve(Captcha captcha, Map<String, Integer> waitOptions) throws Exception {
        captcha.setId(send(captcha));

        if (!lastCaptchaHasCallback) {
            waitForResult(captcha, waitOptions);
        }
    }
    public void waitForResult(Captcha captcha) throws Exception {
        Map<String, Integer> waitOptions = new HashMap<>();

        if (captcha instanceof ReCaptcha) {
            waitOptions.put("timeout", recaptchaTimeout);
        }
        waitForResult(captcha, waitOptions);
    }

    /**
     * This helper waits for captcha result, and when result is ready, returns it
     *
     * @param captcha captcha to wait for result
     * @param waitOptions wait
     * @throws Exception unable to do something
     */
    public void waitForResult(Captcha captcha, Map<String, Integer> waitOptions) throws Exception {
        long startedAt = (long)(System.currentTimeMillis() / 1000);

        int timeout = waitOptions.getOrDefault("timeout", this.defaultTimeout);
        int pollingInterval = waitOptions.getOrDefault("pollingInterval", this.pollingInterval);

        while (true) {
            long now = (long)(System.currentTimeMillis() / 1000);

            if (now - startedAt < timeout) {
                Thread.sleep(pollingInterval * 1000L);
            } else {
                break;
            }

            try {
                String result = getResult(captcha.getId());
                if (result != null) {
                    captcha.setCode(result);
                    return;
                }
            } catch (NetworkException e) {
                // ignore network errors
            }
        }

        throw new TimeoutException("Timeout " + timeout + " seconds reached");
    }

    /**
     * Sends captcha to '/in.php', and returns its `id`
     *
     * @param captcha
     * @return
     * @throws Exception
     */
    public String send(Captcha captcha) throws Exception {
        Map<String, String> params = captcha.getParams();
        Map<String, File> files = captcha.getFiles();

        sendAttachDefaultParams(params);

        validateFiles(files);

        String response = apiClient.in(params, files);

        if (!response.startsWith("OK|")) {
            throw new ApiException("Cannot recognise api response (" + response + ")");
        }

//        captcha.setId(response.substring(3));

        return response.substring(3);
    }

    /**
     * Returns result of captcha if it was solved or `null`, if result is not ready
     *
     * @param id
     * @return
     * @throws Exception
     */
    public String getResult(String id) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("action", "get");
        params.put("id", id);

        String response = res(params);

        if (response.equals("CAPCHA_NOT_READY")) {
            return null;
        }

        if (!response.startsWith("OK|")) {
            throw new ApiException("Cannot recognise api response (" + response + ")");
        }

        return response.substring(3);
    }

    /**
     * Gets account's balance
     *
     * @return balance
     * @throws Exception unable to parse throw error
     */
    public double balance() throws Exception {
        String response = res("getbalance");
        return Double.parseDouble(response);
    }

    /**
     * Reports if captcha was solved correctly (sends `reportbad` or `reportgood` to `/res.php`)
     *
     * @param id captcha id
     * @param correct send report type
     * @throws Exception unable to report throw error
     */
    public void report(String id, boolean correct) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);

        if (correct) {
            params.put("action", "reportgood");
        } else {
            params.put("action", "reportbad");
        }

        res(params);
    }

    /**
     * Makes request to `/res.php`
     *
     * @param action action to do
     * @return result
     * @throws Exception unable to do something
     */
    private String res(String action) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("action", action);
        return res(params);
    }

    /**
     * Makes request to `/res.php`
     *
     * @param params
     * @return
     * @throws Exception
     */
    private String res(Map<String, String> params) throws Exception {
        params.put("key", apiKey);
        return apiClient.res(params);
    }

    /**
     * Attaches default parameters to request
     *
     * @param params
     */
    private void sendAttachDefaultParams(Map<String, String> params) {
        params.put("key", apiKey);

        if (callback != null) {
            if (!params.containsKey("pingback")) {
                params.put("pingback", callback);
            } else if (params.get("pingback").length() == 0) {
                params.remove("pingback");
            }
        }

        lastCaptchaHasCallback = params.containsKey("pingback");

        if (softId != 0 && !params.containsKey("soft_id")) {
            params.put("soft_id", String.valueOf(softId));
        }
    }

    private void validateFiles(Map<String, File> files) throws ValidationException {
        for (Map.Entry<String, File> entry : files.entrySet()) {
            File file = entry.getValue();

            if (!file.exists()) {
                throw new ValidationException("File not found: " + file.getAbsolutePath());
            }

            if (!file.isFile()) {
                throw new ValidationException("Resource is not a file: " + file.getAbsolutePath());
            }
        }
    }

}