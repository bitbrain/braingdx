![logo](logo.png)
-
:video_game: Game jam Java framework based on [libgdx](https://libgdx.badlogicgames.com/).

[![Build Status](https://travis-ci.org/bitbrain/braingdx.svg?branch=master)](https://travis-ci.org/bitbrain/braingdx)

**:space_invader: [Features](#features) |**
**:package: [Installation](#installation) |**
**:green_book: [Documentation](https://github.com/bitbrain/braingdx/wiki) |**
**:beers: [Collaboration](#collaboration) |**
**:rocket: [License](#license) |**
**:pencil: [Changelog](CHANGELOG.md)**

---

# Features

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

```xml
<dependency>
   <artifactId>braingdx</artifactId>
   <groupId>com.github.myrealitycoding</groupId>
   <version>{not available yet}</version>
</dependency>
```

# Collaboration

Feel free to create [a new pull request](https://github.com/bitbrain/braingdx/pull/new/master). When you detect an issue [please report it here](https://github.com/MyRealityCoding/braingdx/issues).

# License

This software is licensed under the [Apache 2 License](LICENSE).

# Special thanks

I would like to thank [Mario Zechner](https://twitter.com/badlogicgames) and the [libgdx](https://libgdx.badlogicgames.com/) community for developing such an amazing framework to work with.
Also special thanks to [Aurelien Ribon](http://www.aurelienribon.com/blog/projects/universal-tween-engine) for creating a Java tween engine which can be used all over the place. This project also includes his work.
