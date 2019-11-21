[![Build Status](https://travis-ci.org/Fewlaps/slim-jpg.svg?branch=master)](https://travis-ci.org/Fewlaps/slim-jpg)
[![Download](https://api.bintray.com/packages/fewlaps/maven/slimjpg/images/download.svg) ](https://bintray.com/fewlaps/maven/slimjpg/_latestVersion)
# SlimJPG

This library will convert your JPG, PNG, GIF and BMP pictures to optimized JPG ones.

The project started as a fork of [collicalex/JPEGOptimizer](https://github.com/collicalex/JPEGOptimizer), a desktop application to bulk optimize JPG files.


## Why is this library so great?

Compressing pictures by the old way, just setting the JPEG compression quality when saving the file, leads to unknown visual quality loss. You can save a picture in 70% JPEG quality and still have a great picture while saving another one with the same 70% quality and get a very bad result. That's because the JPEG quality doesn't know anything about the result. In the other hand, SlimJPG does.

Instead of setting the JPEG quality, SlimJPG receives a maximum visual difference. That's the porcentage of pixels that could vary from the original picture. So, if you set a 0% difference, you can still get an optimized image. Or, if you prefer to lose a bit of visual quality, like 0,5%, SlimJPG will compress your image as much as possible, just finding the best JPEG compression that matches your criteria. 

In addition, you can set a max file weight. You can request an optimized picture that doesn't weight more than 50kB. SlimJPG will give you the best picture that JPEG compression can.

What about the picture's metadata? For example, if you're concerned with removing the user's geolocation, you can delete it from the resulting picture. In the other hand, if you prefer to keep the Color Profile from the original source, you can choose to keep it. In addition, if you don't mind to keep or to discard the metadata, SlimJPG will give you a picture with or without it, choosing the option that gives a smaller file.


## Ok, show me the code

```java
// An almost lossless optimization of your image
SlimJpg.file(byteArray).optimize();
SlimJpg.file(inputStream).optimize();
SlimJpg.file(file).optimize();

// Optimize your picture with a 0.5% of maximum visual loss
SlimJpg.file(picture)
                .maxVisualDiff(0.5)
                .optimize();

// Optimize your picture to fit a 50kB file
SlimJpg.file(picture)
                .maxFileWeightInKB(50)
                .optimize();

// An almost lossless optimization deleting the metadata
SlimJpg.file(picture)
                .deleteMetadata()
                .optimize();

// An almost lossless optimization keeping the metadata
SlimJpg.file(picture)
                .keepMetadata()
                .optimize();

// An almost lossless optimization choosing the optimised metadata value
SlimJpg.file(picture)
                .useOptimizedMetadata()
                .optimize();

// Use the whole criteria the API offers
SlimJpg.file(picture)
                .maxVisualDiff(0.5)
                .maxFileWeightInKB(50)
                .deleteMetadata()
                .optimize();
```


## Give me some numbers!

Of course! These computations have been made in a MacBook PRO 13" 2015 with only two warm cores.

**A perfect copy:** Optimizing a picture without losing anything:

|Resolution|Original size|Saved|JPEG Quality|Took|
|:---:|:---:|:---:|:---:|:---:|
|1280x1280|206.98kB|2.51kB (1.21%)|79%|2,381ms|
|800x600|525.95kB|301.78kB (57.38%)|93%|2,845ms|

**A common optimization:** Optimizing a picture losing 0.5% with a 200kB max file size, deleting the metadata:

|Resolution|Original size|Saved|JPEG Quality|Took|
|:---:|:---:|:---:|:---:|:---:|
|1280x1280|206.98kB|11.93kB (5.76%)|74%|1,223ms|
|800x600|525.95kB|326.76kB (62.13%)|91%|678ms|

**A very small file:** Optimizing a picture losing 1% with a 50kB max file size, keeping the metadata:

|Resolution|Original size|Saved|JPEG Quality|Took|
|:---:|:---:|:---:|:---:|:---:|
|1280x1280|206.98kB|157.96kB (76.32%)|10%*|1,356ms|
|800x600|525.95kB|476.20kB (90.54%)|25%*|504ms|

*You'll notice the last test used very low JPEG quality. That's because the file size was too small, so it used the highest quality that gave <50kB files.


## May I use it Android?

Nope. SlimJpeg targets the rocky server-side guys. The library uses `javax.imageio.ImageIO` that is not included in Android. D'oh! If you try it you'll end with a `java.lang.NoClassDefFoundError: Failed resolution of: Ljavax/imageio/ImageIO;`. It was harder for me than for you...

## How does SlimJPG manage the color profiles?

The embedded color profile of a JPG is stored in its metadata. So, if you have a JPG with an embedded color profile and you need to have it embedded also in the optimized JPG, tell the library to keep the picture's metadata. This is usually needed when you intend to print that optimized JPG on paper or fabric, like the photographs or the fashion designers do. If you plan to display your images on computer or phone screens, usually you don't need to keep the color profiles.

In case you're managing JPGs with an embedded color profile and you delete its metadata, the resulting JPG will have no color profile, so the default sRGB will be used. That's what the JPG standard says. But don't worry: if the source picture had another color profile, let's say Adobe RGB 1998, SlimJPG will convert those colors to sRGB and save the picture without a color profile, so the browser or app that displays the JPG will successfully use sRGB.


# Download

* Gradle:
```groovy
repositories { jcenter() }
    
compile 'com.fewlaps.slimjpg:slimjpg:1.3.1'
```
* Maven:
```xml
<repository>
  <id>fewlaps</id>
  <url>https://dl.bintray.com/fewlaps/maven</url>
</repository>

<dependency>
    <groupId>com.fewlaps.slimjpg</groupId>
    <artifactId>slimjpg</artifactId>
    <version>1.3.1</version>
</dependency>
```

## License

Copyright (c) 2019 Fewlaps (https://github.com/fewlaps) & Alex (https://github.com/collicalex)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
