<a href="https://github.com/2captcha/2captcha-python"><img src="https://github.com/user-attachments/assets/37e1d860-033b-4cf3-a158-468fc6b4debc" width="82" height="30"></a>
<a href="https://github.com/2captcha/2captcha-javascript"><img src="https://github.com/user-attachments/assets/4d3b4541-34b2-4ed2-a687-d694ce67e5a6" width="36" height="30"></a>
<a href="https://github.com/2captcha/2captcha-go"><img src="https://github.com/user-attachments/assets/ab22182e-6cb2-41fa-91f4-d5e89c6d7c6f" width="63" height="30"></a>
<a href="https://github.com/2captcha/2captcha-ruby"><img src="https://github.com/user-attachments/assets/0270d56f-79b0-4c95-9b09-4de89579914b" width="75" height="30"></a>
<a href="https://github.com/2captcha/2captcha-cpp"><img src="https://github.com/user-attachments/assets/36de8512-acfd-44fb-bb1f-b7c793a3f926" width="45" height="30"></a>
<a href="https://github.com/2captcha/2captcha-php"><img src="https://github.com/user-attachments/assets/e8797843-3f61-4fa9-a155-ab0b21fb3858" width="52" height="30"></a>
<a href="https://github.com/2captcha/2captcha-java"><img src="https://github.com/user-attachments/assets/1c576ba8-aa8d-4d6f-b1a4-b346473a4e1b" width="50" height="30"></a>
<a href="https://github.com/2captcha/2captcha-csharp"><img src="https://github.com/user-attachments/assets/f4d449de-780b-49ed-bb0a-b70c82ec4b32" width="38" height="30"></a>

# JAVA Module for 2Captcha API (captcha solver)
The easiest way to quickly integrate [2Captcha] into your code to automate solving of any types of captcha.
Examples of API requests for different captcha types are available on the [Java captcha solver] page.

- [JAVA Module for 2Captcha API (captcha solver)](#java-module-for-2captcha-api-captcha-solver)
  - [Installation](#installation)
  - [Configuration](#configuration)
    - [TwoCaptcha instance options](#twocaptcha-instance-options)
  - [Solve captcha](#solve-captcha)
    - [Captcha options](#captcha-options)
    - [Basic example](#basic-example)
    - [Normal Captcha](#normal-captcha)
    - [Text Captcha](#text-captcha)
    - [reCAPTCHA v2](#recaptcha-v2)
    - [reCAPTCHA v3](#recaptcha-v3)
    - [FunCaptcha](#funcaptcha)
    - [GeeTest](#geetest)
    - [GeeTest v4](#geetest-v4)
    - [KeyCaptcha](#keycaptcha)
    - [Capy](#capy)
    - [Grid](#grid)
    - [Canvas](#canvas)
    - [ClickCaptcha](#clickcaptcha)
    - [Rotate](#rotate)
    - [Audio Captcha](#audio-captcha)
    - [Yandex](#yandex)
    - [Lemin Cropped Captcha](#lemin-cropped-captcha)
    - [Cloudflare Turnstile](#cloudflare-turnstile)
    - [Amazon WAF](#amazon-waf)
    - [Friendly Captcha](#friendly-captcha)
    - [MTCaptcha](#mtcaptcha)
    - [Tencent](#tencent)
    - [Cutcaptcha](#cutcaptcha)
    - [atbCAPTCHA](#atbcaptcha)
    - [CyberSiARA](#cybersiara)
    - [DataDome](#datadome)
  - [Other methods](#other-methods)
    - [send / getResult](#send--getresult)
    - [balance](#balance)
    - [report](#report)
  - [Proxies](#proxies)
  - [Error handling](#error-handling)
  - [Examples](#examples)
- [Get in touch](#get-in-touch)
- [Join the team 👪](#join-the-team-)
- [License](#license)
  - [Graphics and Trademarks](#graphics-and-trademarks)

## Installation
2captcha-java artifact is available in [Maven Central]

## Configuration
`TwoCaptcha` instance can be created like this:
```java
TwoCaptcha solver = new TwoCaptcha('YOUR_API_KEY');
```
Also there are few options that can be configured:
```java
solver.setHost("2captcha.com");
solver.setSoftId(123);
solver.setCallback("https://your.site/result-receiver");
solver.setDefaultTimeout(120);
solver.setRecaptchaTimeout(600);
solver.setPollingInterval(10);
solver.setExtendedResponse(1);
```

### TwoCaptcha instance options

| Option           | Default value| Description                                                                                                                                        |
|------------------| ------------ |----------------------------------------------------------------------------------------------------------------------------------------------------|
| host             |`2captcha.com`| API server. You can set it to `rucaptcha.com` if your account is registered there                                                                  |
| softId           | 4581         | your software ID obtained after publishing in [2captcha software catalog]                                                                          |
| callback         | -            | URL of your web-sever that receives the captcha recognition result. The URl should be first registered in [pingback settings] of your account      |
| defaultTimeout   | 120          | Polling timeout in seconds for all captcha types except reCAPTCHA. Defines how long the module tries to get the answer from `res.php` API endpoint |
| recaptchaTimeout | 600          | Polling timeout for reCAPTCHA in seconds. Defines how long the module tries to get the answer from `res.php` API endpoint                          |
| pollingInterval  | 10           | Interval in seconds between requests to `res.php` API endpoint, setting values less than 5 seconds is not recommended                              |
| extendedResponse | 0            | Json or String format response from `res.php` API endpoint, extendedResponse = 1 returns JSON format response. Suitable for [ClickCaptcha](#clickcaptcha), [Canvas](#canvas) |

>  [!IMPORTANT]
>  Once *callback URL* is defined for `TwoCaptcha` instance with `setCallback`, all methods return only the captcha ID and DO NOT poll the API to get the result. The result will be sent to the callback URL.
To get the answer manually use [getResult method](#send--getresult)

> [!TIP]
> Use the `extendedResponse(1)` setting to obtain an extended response for the captcha. When using `extendedResponse(1)`, the response will be presented as a `JSON` string. Utilizing the extended response `extendedResponse(1)` will be helpful when solving captchas such as [ClickCaptcha](#clickcaptcha). 

## Solve captcha
When you submit any image-based captcha use can provide additional options to help 2captcha workers to solve it properly.

### Captcha options
| Option        | Default Value | Description                                                                                        |
| ------------- | ------------- | -------------------------------------------------------------------------------------------------- |
| numeric       | 0             | Defines if captcha contains numeric or other symbols [see more info in the API docs][post options] |
| minLength     | 0             | minimal answer length                                                                              |
| maxLength     | 0             | maximum answer length                                                                              |
| phrase        | 0             | defines if the answer contains multiple words or not                                               |
| caseSensitive | 0             | defines if the answer is case sensitive                                                            |
| calc          | 0             | defines captcha requires calculation                                                               |
| lang          | -             | defines the captcha language, see the [list of supported languages]                                |
| hintImg       | -             | an image with hint shown to workers with the captcha                                               |
| hintText      | -             | hint or task text shown to workers with the captcha                                                |

Below you can find basic examples for every captcha type. Check out [examples directory] to find more examples with all available options.

### Basic example
Example below shows a basic solver call example with error handling.

```java
Normal captcha = new Normal();
captcha.setFile("path/to/captcha.jpg");
captcha.setMinLen(4);
captcha.setMaxLen(20);
captcha.setCaseSensitive(true);
captcha.setLang("en");

try {
    solver.solve(captcha);
    System.out.println("Captcha solved: " + captcha.getCode());
} catch (Exception e) {
    System.out.println("Error occurred: " + e.getMessage());
}
```

### Normal Captcha

<sup>[API method description.](https://2captcha.com/2captcha-api#solving_normal_captcha)</sup>

To bypass a normal captcha (distorted text on image) use the following method. This method also can be used to recognize any text on the image.

```java
Normal captcha = new Normal();
captcha.setFile("path/to/captcha.jpg");
captcha.setNumeric(4);
captcha.setMinLen(4);
captcha.setMaxLen(20);
captcha.setPhrase(true);
captcha.setCaseSensitive(true);
captcha.setCalc(false);
captcha.setLang("en");
captcha.setHintImg(new File("path/to/hint.jpg"));
captcha.setHintText("Type red symbols only");
```

### Text Captcha

<sup>[API method description.](https://2captcha.com/2captcha-api#solving_text_captcha)</sup>

This method can be used to bypass a captcha that requires to answer a question provided in clear text.

```java
Text captcha = new Text();
captcha.setText("If tomorrow is Saturday, what day is today?");
captcha.setLang("en");
```

### reCAPTCHA v2

<sup>[API method description.](https://2captcha.com/2captcha-api#solving_recaptchav2_new)</sup>

Use this method to solve reCAPTCHA V2 and obtain a token to bypass the protection.

```java
ReCaptcha captcha = new ReCaptcha();
captcha.setSiteKey("6Le-wvkSVVABCPBMRTvw0Q4Muexq1bi0DJwx_mJ-");
captcha.setUrl("https://mysite.com/page/with/recaptcha");
captcha.setInvisible(true);
captcha.setEnterprise(true);
captcha.setAction("verify");
captcha.setProxy("HTTPS", "login:password@IP_address:PORT");
captcha.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Safari/537.36");
captcha.setCookies("KEY1:Value1;KEY2:Value2;");
```
### reCAPTCHA v3

<sup>[API method description.](https://2captcha.com/2captcha-api#solving_recaptchav3)</sup>

This method provides reCAPTCHA V3 solver and returns a token.

```java
ReCaptcha captcha = new ReCaptcha();
captcha.setSiteKey("6Le-wvkSVVABCPBMRTvw0Q4Muexq1bi0DJwx_mJ-");
captcha.setUrl("https://mysite.com/page/with/recaptcha");
captcha.setVersion("v3");
captcha.setEnterprise(false);
captcha.setAction("verify");
captcha.setScore(0.3);
captcha.setProxy("HTTPS", "login:password@IP_address:PORT");
```

### FunCaptcha

<sup>[API method description.](https://2captcha.com/2captcha-api#solving_funcaptcha_new)</sup>

FunCaptcha (Arkoselabs) solving method. Returns a token.

```java
FunCaptcha captcha = new FunCaptcha();
captcha.setSiteKey("69A21A01-CC7B-B9C6-0F9A-E7FA06677FFC");
captcha.setUrl("https://mysite.com/page/with/funcaptcha");
captcha.setSUrl("https://client-api.arkoselabs.com");
captcha.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36");
captcha.setData("anyKey", "anyValue");
captcha.setProxy("HTTPS", "login:password@IP_address:PORT");
```

### GeeTest

<sup>[API method description.](https://2captcha.com/2captcha-api#solving_geetest)</sup>

Method to solve GeeTest puzzle captcha. Returns a set of tokens as JSON.

```java
GeeTest captcha = new GeeTest();
captcha.setGt("f2ae6cadcf7886856696502e1d55e00c");
captcha.setApiServer("api-na.geetest.com");
captcha.setChallenge("12345678abc90123d45678ef90123a456b");
captcha.setUrl("https://mysite.com/captcha.html");
captcha.setProxy("HTTPS", "login:password@IP_address:PORT");
```

### GeeTest v4

<sup>[API method description.](https://2captcha.com/2captcha-api#geetest-v4)</sup>

Method to solve GeeTestV4 puzzle captcha. Returns a set of tokens as JSON.

```java
GeeTestV4 captcha = new GeeTestV4();
captcha.setCaptchaId("72bf15796d0b69c43867452fea615052");
captcha.setApiServer("api-na.geetest.com");
captcha.setChallenge("12345678abc90123d45678ef90123a456b");
captcha.setUrl("https://mysite.com/captcha.html");
captcha.setProxy("HTTPS", "login:password@IP_address:PORT");
```

### KeyCaptcha

<sup>[API method description.](https://2captcha.com/2captcha-api#solving_keycaptcha)</sup>

Token-based method to solve KeyCaptcha.

```java
KeyCaptcha captcha = new KeyCaptcha();
captcha.setUserId(10);
captcha.setSessionId("493e52c37c10c2bcdf4a00cbc9ccd1e8");
captcha.setWebServerSign("9006dc725760858e4c0715b835472f22");
captcha.setWebServerSign2("2ca3abe86d90c6142d5571db98af6714");
captcha.setUrl("https://www.keycaptcha.ru/demo-magnetic/");
captcha.setProxy("HTTPS", "login:password@IP_address:PORT");
```

### Capy

<sup>[API method description.](https://2captcha.com/2captcha-api#solving_capy)</sup>

Token-based method to bypass Capy puzzle captcha.

```java
Capy captcha = new Capy();
captcha.setSiteKey("PUZZLE_Abc1dEFghIJKLM2no34P56q7rStu8v");
captcha.setUrl("https://www.mysite.com/captcha/");
captcha.setProxy("HTTPS", "login:password@IP_address:PORT");
```

### Grid

<sup>[API method description.](https://2captcha.com/2captcha-api#grid)</sup>

Grid method is originally called Old reCAPTCHA V2 method. The method can be used to bypass any type of captcha where you can apply a grid on image and need to click specific grid boxes. Returns numbers of boxes.

```java
Grid captcha = new Grid();
captcha.setFile("path/to/captcha.jpg");
captcha.setRows(3);
captcha.setCols(3);
captcha.setPreviousId(0);
captcha.setCanSkip(false);
captcha.setLang("en");
captcha.setHintImg(new File("path/to/hint.jpg"));
captcha.setHintText("Select all images with an Orange");
```

### Canvas

<sup>[API method description.](https://2captcha.com/2captcha-api#canvas)</sup>

Canvas method can be used when you need to draw a line around an object on image. Returns a set of points' coordinates to draw a polygon.

```java
Canvas captcha = new Canvas();
captcha.setFile("path/to/captcha.jpg");
captcha.setPreviousId(0);
captcha.setCanSkip(false);
captcha.setLang("en");
captcha.setHintImg(new File("path/to/hint.jpg"));
captcha.setHintText("Draw around apple");
```

### ClickCaptcha

<sup>[API method description.](https://2captcha.com/2captcha-api#coordinates)</sup>

ClickCaptcha method returns coordinates of points on captcha image. Can be used if you need to click on particular points on the image.

```java
Coordinates captcha = new Coordinates();
captcha.setFile("path/to/captcha.jpg");
captcha.setLang("en");
captcha.setHintImg(new File("path/to/hint.jpg"));
captcha.setHintText("Select all images with an Orange");
```

### Rotate

<sup>[API method description.](https://2captcha.com/2captcha-api#solving_rotatecaptcha)</sup>

This method can be used to solve a captcha that asks to rotate an object. Mostly used to bypass FunCaptcha. Returns the rotation angle.

```java
Rotate captcha = new Rotate();
captcha.setFile("path/to/captcha.jpg");
captcha.setAngle(40);
captcha.setLang("en");
captcha.setHintImg(new File("path/to/hint.jpg"));
captcha.setHintText("Put the images in the correct way up");
```

### Audio Captcha

<sup>[API method description.](https://2captcha.com/2captcha-api#audio)</sup>

Use the following method to bypass an audio captcha (mp3 formats only). 
You must provide the language as `lang = 'en'`. Supported languages are "en", "ru", "de", "el", "pt", "fr".

```java
TwoCaptcha solver = new TwoCaptcha(args[0]);
byte[] bytes = Files.readAllBytes(Paths.get("src/main/resources/audio-en.mp3"));
String base64EncodedImage = Base64.getEncoder().encodeToString(bytes);
Audio captcha = new Audio();
captcha.setBase64(base64EncodedImage);
```

### Yandex
Use this method to solve Yandex and obtain a token to bypass the protection.

```java
TwoCaptcha solver = new TwoCaptcha(args[0]);
Yandex captcha = new Yandex();
captcha.setSiteKey("Y5Lh0tiycconMJGsFd3EbbuNKSp1yaZESUOIHfeV");
captcha.setUrl("https://rutube.ru");
```

### Lemin Cropped Captcha

<sup>[API method description.](https://2captcha.com/2captcha-api#lemin)</sup>

Use this method to solve Lemin and obtain a token to bypass the protection.

```java
TwoCaptcha solver = new TwoCaptcha(args[0]);
Lemin captcha = new Lemin();      
captcha.setCaptchaId("CROPPED_d3d4d56_73ca4008925b4f83a8bed59c2dd0df6d");
captcha.setUrl("https://mysite.com/page/with/LeminCaptcha");
captcha.setApiServer("api.leminnow.com");
```

### Cloudflare Turnstile

<sup>[API method description.](https://2captcha.com/2captcha-api#turnstile)</sup>

Use this method to solve Cloudflare Turnstile and obtain a token to bypass the protection.

```java
TwoCaptcha solver = new TwoCaptcha(args[0]);
Turnstile captcha = new Turnstile();
captcha.setSiteKey("0x4AAAAAAAChNiVJM_WtShFf");
captcha.setUrl("https://ace.fusionist.io");
```

### Amazon WAF

<sup>[API method description.](https://2captcha.com/2captcha-api#amazon-waf)</sup>

Use this method to solve Amazon WAF and obtain a token to bypass the protection.

```java
TwoCaptcha solver = new TwoCaptcha(args[0]);
AmazonWaf captcha = new AmazonWaf();
captcha.setSiteKey("AQIDAHjcYu/GjX+QlghicBgQ/7bFaQZ+m5FKCMDnO+vTbNg96AF5H1K/siwSLK7RfstKtN5bAAAAfjB8BgkqhkiG9w0BBwagbzBtAgEAMGgGCSqGSIb3DQEHATAeBglg");
captcha.setUrl("https://non-existent-example.execute-api.us-east-1.amazonaws.com");
captcha.setIV("test_iv");
captcha.setContext("test_context");
```

### Friendly Captcha

<sup>[API method description.](https://2captcha.com/2captcha-api#friendly-captcha)</sup>

Use this method to solve Friendly Captcha and obtain a token to bypass the protection.

```java
TwoCaptcha solver = new TwoCaptcha(args[0]);
FriendlyCaptcha friendlyCaptcha = new FriendlyCaptcha();
friendlyCaptcha.setSiteKey("FCMST5VUMCBOCGQ9");
friendlyCaptcha.setPageUrl("https://mysite.com/page/with/FriendlyCaptcha");
```

### MTCaptcha

<sup>[API method description.](https://2captcha.com/2captcha-api#mtcaptcha)</sup>

Use this method to solve MTCaptcha and obtain a token to bypass the protection.

```java
TwoCaptcha solver = new TwoCaptcha(args[0]);
MtCaptcha mtCaptcha = new MtCaptcha();
mtCaptcha.setSiteKey("MTPublic-KzqLY1cKH");
mtCaptcha.setPageUrl("https://2captcha.com/demo/mtcaptcha");
```

### Tencent

<sup>[API method description.](https://2captcha.com/2captcha-api#tencent)</sup>

Use this method to solve Tencent and obtain a token to bypass the protection.

```java
TwoCaptcha solver = new TwoCaptcha(args[0]);
Tencent tencent = new Tencent();
tencent.setAppId("2092215077");
tencent.setPageUrl("https://mysite.com/page/with/tencent");
```
### Cutcaptcha

<sup>[API method description.](https://2captcha.com/2captcha-api#cutcaptcha)</sup>

Use this method to solve Cutcaptcha and obtain a token to bypass the protection.

```java
TwoCaptcha solver = new TwoCaptcha(args[0]);
Cutcaptcha cutcaptcha = new Cutcaptcha();
cutcaptcha.setMiseryKey("a1488b66da00bf332a1488993a5443c79047e752");
cutcaptcha.setUrl("https://filecrypt.co/Container/237D4D0995.html");
cutcaptcha.setDataApiKey("SAb83IIB");
```

### atbCAPTCHA

<sup>[API method description.](https://2captcha.com/2captcha-api#atb-captcha)</sup>

Use this method to solve atbCAPTCHA and obtain a token to bypass the protection.

```java
AtbCAPTCHA captcha = new AtbCAPTCHA();
captcha.setAppId("af23e041b22d000a11e22a230fa8991c");
captcha.setPageUrl("https://www.playzone.vip/");
captcha.setApiServer("https://cap.aisecurius.com");
```

### CyberSiARA

<sup>[API method description.](https://2captcha.com/2captcha-api#cybersiara)</sup>

Use this method to solve CyberSiARA and obtain a token to bypass the protection.

```java
Cybersiara captcha = new Cybersiara();
captcha.setMasterUrlId("tpjOCKjjpdzv3d8Ub2E9COEWKt1vl1Mv");
captcha.setUrl("https://demo.mycybersiara.com/");
captcha.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36");
```

### DataDome

<sup>[API method description.](https://2captcha.com/2captcha-api#datadome)</sup>

Use this method to solve DataDome and obtain a token to bypass the protection.

> [!IMPORTANT]
> To solve the DataDome captcha, you must use a proxy. It is recommended to use [residential proxies].

```java
DataDome captcha = new DataDome();
captcha.setCaptchaUrl("https://geo.captcha-delivery.com/captcha/?initialCid=AHrlqAAA...P~XFrBVptk&t=fe&referer=https%3A%2F%2Fhexample.com&s=45239&e=c538be..c510a00ea");
captcha.setUrl("https://example.com/");
captcha.setUserAgent("Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Mobile Safari/537.3");
captcha.setProxy("HTTPS", "login:password@IP_address:PORT");
```

## Other methods

### send / getResult
These methods can be used for manual captcha submission and answer polling.

```java
String captchaId = solver.send(captcha);

Thread.sleep(20 * 1000);

String code = solver.getResult(captchaId);
```
### balance

<sup>[API method description.](https://2captcha.com/2captcha-api#additional-methods)</sup>

Use this method to get your account's balance

```java
double balance = solver.balance();
```
### report

<sup>[API method description.](https://2captcha.com/2captcha-api#complain)</sup>

Use this method to report good or bad captcha answer.

```java
solver.report(captcha.getId(), true); // captcha solved correctly
solver.report(captcha.getId(), false); // captcha solved incorrectly
```

## Proxies
You can pass your proxy as an additional argument for methods: recaptcha, funcaptcha, geetest, geetest v4, keycaptcha, capy puzzle, lemin, turnstile, amazon waf and etc. The proxy will be forwarded to the API to solve the captcha.

We have our own proxies that we can offer you. [Buy residential proxies](https://2captcha.com/proxy/residential-proxies) for avoid restrictions and blocks. [Quick start](https://2captcha.com/proxy?openAddTrafficModal=true).

```java
captcha.setProxy("HTTPS", "login:password@IP_address:PORT");
```


## Error handling
If case of an error captcha solver throws an exception. It's important to properly handle these cases. We recommend to use `try catch` to handle exceptions. The list of all errors can be found in the [API documentation](https://2captcha.com/2captcha-api#list-of-inphp-errors).

```java
try {
    solver.solve(captcha);
} catch (ValidationException e) {
    // invalid parameters passed
} catch (NetworkException e) {
    // network error occurred
} catch (ApiException e) {
    // api respond with error
} catch (TimeoutException e) {
    // captcha is not solved so far
}
```

## Examples
Examples of solving all supported captcha types are located in the [examples] directory.

## Get in touch

<a href="mailto:support@2captcha.com"><img src="https://github.com/user-attachments/assets/539df209-7c85-4fa5-84b4-fc22ab93fac7" width="80" height="30"></a>
<a href="https://2captcha.com/support/tickets/new"><img src="https://github.com/user-attachments/assets/be044db5-2e67-46c6-8c81-04b78bd99650" width="81" height="30"></a>

## Join the team 👪

There are many ways to contribute, of which development is only one! Find your next job. Open positions: AI experts, scrapers, developers, technical support, and much more! 😍

<a href="mailto:job@2captcha.com"><img src="https://github.com/user-attachments/assets/36d23ef5-7866-4841-8e17-261cc8a4e033" width="80" height="30"></a>

## License

The code in this repository is licensed under the MIT License. See the [LICENSE](./LICENSE) file for more details.

### Graphics and Trademarks

The graphics and trademarks included in this repository are not covered by the MIT License. Please contact <a href="mailto:support@2captcha.com">support</a> for permissions regarding the use of these materials.

<!-- Shared links -->
[Maven Central]: https://search.maven.org/artifact/com.github.2captcha/2captcha-java
[Examples directory]: ./src/main/java/examples
[examples]: ./src/main/java/examples
[2Captcha]: https://2captcha.com/
[2captcha software catalog]: https://2captcha.com/software
[Pingback settings]: https://2captcha.com/setting/pingback
[Post options]: https://2captcha.com/2captcha-api#normal_post
[List of supported languages]: https://2captcha.com/2captcha-api#language
[Java captcha solver]: https://2captcha.com/lang/java
[residential proxies]: https://2captcha.com/proxy/residential-proxies
