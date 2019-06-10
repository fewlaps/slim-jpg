[![Build Status](https://travis-ci.org/Fewlaps/slim-jpg.svg?branch=master)](https://travis-ci.org/Fewlaps/slim-jpg)
[![Download](https://api.bintray.com/packages/fewlaps/maven/slimjpg/images/download.svg) ](https://bintray.com/fewlaps/maven/slimjpg/_latestVersion)
# SlimJPG

This library will convert your JPG, PNG, GIF and BMP pictures to optimized JPG ones.

The project started as a fork of [collicalex/JPEGOptimizer](https://github.com/collicalex/JPEGOptimizer), a desktop application to bulk optimize JPG files.


## Why is this library so great?

Compressing pictures by the old way, just setting the JPEG compression quality when saving the file, leads to unknown visual quality loss. You can save a picture in 70% JPEG quality and still have a great picture while saving another one with the same 70% quality and get a very bad result. That's because the JPEG quality doesn't know anything about the result. In the other hand, SlimJPG does.

Instead of setting the JPEG quality, SlimJPG receives a maximum visual difference. That's the porcentage of pixels that could vary from the original picture. So, if you set a 0% difference, you can still get an optimized image. Or, if you prefer to lose a bit of visual quality, like 0,5%, SlimJPG will compress your image as much as possible, just finding the best JPEG compression that matches your criteria. 

In addition, you can set a max file weight. You can request an optimized picture that doesn't weight more than 50kB. SlimJPG will give you the best picture that JPEG compression can.

Are you concerned about your user's privacy? SlimJPG can keep the metadata or delete it of the resulting picture.


## Ok, show me the code

```java
// Optimize your picture without losing anything
SlimJpg.file(yourPictureBytes)
                .optimize();

// Optimize your picture with losing a 0.5% of visual quality
SlimJpg.file(yourPictureBytes)
                .maxVisualDiff(0.5)
                .optimize();

// Optimize your picture to fit a 50kB file
SlimJpg.file(yourPictureBytes)
                .maxFileWeightInKB(50)
                .optimize();

// Optimize your picture deleting the metadata
SlimJpg.file(yourPictureBytes)
                .deleteMetadata()
                .optimize();

// Optimize your picture keeping the metadata
SlimJpg.file(yourPictureBytes)
                .keepMetadata()
                .optimize();

// Use the whole criteria
SlimJpg.file(getBytes())
                .maxVisualDiff(0.5)
                .maxFileWeightInKB(50)
                .deleteMetadata()
                .optimize();
```


## Give me some numbers!

Of course! These computations have been made in a MacBook PRO 13" 2015 with only two warm cores.

A perfect copy: Optimizing a picture without losing anything:<br/>
1280x1280 206.98kB picture: saved 2.51kB (1.21%), used JPEG quality 79%, took 2,381ms<br/>
800x600 525.95kB picture: saved 301.78kB (57.38%), used JPEG quality 93%, took 2,845ms<br/>

A common optimization: Optimizing a picture losing 0.5% with a 200kB max file size, deleting the metadata:<br/>
1280x1280 206.98kB picture: saved 11.93kB (5.76%), used JPEG quality 74%, took 1,223ms<br/>
800x600 525.95kB picture: saved 326.76kB (62.13%), used JPEG quality 91%, took 678ms<br/>

A very small file: Optimizing a picture losing 1% with a 50kB max file size, keeping the metadata:<br/>
1280x1280 206.98kB picture: saved 157.96kB (76.32%), used JPEG quality 10%, took 1,356ms<br/>
800x600 525.95kB picture: saved 476.20kB (90.54%), used JPEG quality 25%, took 504ms<br/>

You'll notice the last test used very low JPEG quality. That's because the file size was too small, so it used the highest quality that gave <50kB files.


## May I use it Android?

Nope. SlimJpeg targets the rocky server-side guys. The library uses `javax.imageio.ImageIO` that is not included in Android. D'oh! If you try it you'll end with a `java.lang.NoClassDefFoundError: Failed resolution of: Ljavax/imageio/ImageIO;`. It was harder for me than for you...


# Download

* Gradle:
```groovy
repositories { jcenter() }
    
compile 'com.fewlaps.slimjpg:slimjpg:1.0.2'
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
    <version>1.0.2</version>
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
