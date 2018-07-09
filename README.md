![logo](logo.png)
-
:video_game: Game jam Java framework based on [libgdx](https://libgdx.badlogicgames.com/).

[![Build Status](https://travis-ci.org/bitbrain/braingdx.svg?branch=deploy)](https://travis-ci.org/bitbrain/braingdx)
[![mavencentral](https://maven-badges.herokuapp.com/maven-central/io.github.bitbrain/braingdx-core/badge.svg)](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22braingdx-core%22) [![codecov](https://codecov.io/gh/bitbrain/braingdx/branch/deploy/graph/badge.svg)](https://codecov.io/gh/bitbrain/braingdx)
[![license](https://img.shields.io/github/license/bitbrain/braingdx.svg)](LICENSE.MD)

**:space_invader: [Features](#features) |**
**:package: [Installation](#installation) |**
**:green_book: [Documentation](https://github.com/bitbrain/braingdx/wiki) |**
**:beers: [Collaboration](#collaboration) |**
**:rocket: [License](#license) |**
**:pencil: [Changelog](CHANGELOG.md)**

---
# DISCLAIMER! WIKI IS NOT UP-TO-DATE YET!

# Motivation

> Another game development framework? Really?

In Java world, we game developers already have [libgdx](https://libgdx.badlogicgames.com/) in order to create beautiful, feature-rich games. It allows us to be highly flexible how we design our game, however it is not ideal for small game jams. Especially in the first hours time is spent on wiring everything together, creating game object classes, setting up lighting, physics, tweening, writing shaders and particle management as well as sound manipulation. From experience, this can take several hours which is costly, especially on small 24 hour game jams. You could move away from Java and use a popular game engine instead, but we Java developers like it simple and we do *not* want to give up on Java! Alternatively, all the boilerplate work should be done for you - and this is where **braingdx** comes into play!

# Features

This framework provides:

* **Scene transitions** how to switch between screens by applying different and custom animations
* **Tweening pipeline** Using [Universal Tween Engine](https://github.com/AurelienRibon/universal-tween-engine) to make the most of animations in a few lines.
* **World rendering pipeline** no more custom Java classes for game entities. *brainGDX* offers a way to add objects to a game world, handle physics, collision detection and updating out of the box.
* **Lighting** brainGDX comes with [Box2DLights](https://github.com/libgdx/box2dlights) under the hood. Apply and configure dynamic lighting even for your entities.
* **Particles** a game is no game without proper particles. brainGDX comes with full particle support. Attach particles to entities, apply behaviors to particles or define particle fields and effects out of the box.
* **Parallaxing** beautiful parallax scrolling effects await you in this framework.
* **Scene shaders** apply different shaders like Bloom, Antialiasing or Vignette effects to your scenes.
* **Controller support for components** plug in your controller and go ahead. No more configuration hell!
* **Actor library (button menu, nameplates, tooltips)** ever wanted to write an RPG or just show little tooltips on the screen? brainGDX provides several UI components for your needs.
* **Extended Tiled Map support** Ever wanted to write a multi-layered game by using the map editor of your choice? brainGDX provides a framework to render and handle multi-layered 2D maps
* **Entity behaviors** brainGDX comes with a various palette of inbuilt entity behaviors, such as flickering torch lights, WASD movement, random movement or directional movement.

# Installation

## Maven

```xml
<dependency>
    <groupId>io.github.bitbrain</groupId>
    <artifactId>braingdx-core</artifactId>
    <version>$braingdxVersion</version>
</dependency>
```
## Gradle

Add the following to your `build.gradle` file to your `core` module:
```gradle
compile 'io.github.bitbrain:braingdx-core:$braingdxVersion'
compile 'io.github.bitbrain:braingdx-core:$braingdxVersion:sources'
compile 'io.github.bitbrain:braingdx-core:$braingdxVersion:javadoc'
```
Replace `$brainGdxVersion` with the version on Maven Central.
After that you are ready to go!
Learn [here](https://github.com/bitbrain/braingdx/wiki) how to integrate **braingdx** into your **libgdx** game!

# Collaboration

Feel free to create [a new pull request](https://github.com/bitbrain/braingdx/pull/new/master). When you detect an issue [please report it here](https://github.com/bitbrain/braingdx/issues).

# License

This software is licensed under the [Apache 2 License](LICENSE).

# Special thanks

I would like to thank [Mario Zechner](https://twitter.com/badlogicgames) and the [libgdx](https://libgdx.badlogicgames.com/) community for developing such an amazing framework to work with.
Also special thanks to [Aurelien Ribon](http://www.aurelienribon.com/blog/projects/universal-tween-engine) for creating a Java tween engine which can be used all over the place. This project also includes his work.
