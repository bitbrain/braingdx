# Version 0.2.5.2

* Introduce SpriteSheet scaling

# Version 0.2.5.1

* Fix audio issues

# Version 0.2.5

* [[#112](https://github.com/bitbrain/braingdx/issues/112)] 2D audio support: rewritten AudioManager for better audio handling. Also supporting 2D sound.

# Version 0.2.4

* fix class cast exception for collisions

# Version 0.2.3

* [[#110](https://github.com/bitbrain/braingdx/issues/110)] bugfix: TiledMap collisions become sticky on non-collision game objects


# Version 0.2.2

* [[#80](https://github.com/bitbrain/braingdx/issues/80)] Introduce collision debug mode for tiled maps
* [[#105](https://github.com/bitbrain/braingdx/issues/105)] bugfix: NumberFormatException when resizing window
* [[#109](https://github.com/bitbrain/braingdx/issues/109)] LightingManager: clear all lights

# Version 0.2.1

* [[#101](https://github.com/bitbrain/braingdx/issues/101)] Make camera optionally sticky to world bounds

# Version 0.2.0

This version introduces new features such as better event handling and general bug fixes.

* [[#12](https://github.com/bitbrain/braingdx/issues/12)] implemented `ParallaxMap`
* [[#39](https://github.com/bitbrain/braingdx/issues/39)] upgraded `ParticleManager` codebase
* [[#90](https://github.com/bitbrain/braingdx/issues/90)] use 'Fonts' to load ttf fonts instead of BitmapFont
* [[#91](https://github.com/bitbrain/braingdx/issues/91)] introduced BitmapFontBaker 🍞
* [[#94](https://github.com/bitbrain/braingdx/issues/94)] it is now possible to append or prepend additional render pipes
* [[#97](https://github.com/bitbrain/braingdx/issues/97)] implemented Simple Event System

# Version 0.1.9

* reset music position on play

# Version 0.1.8

* global behaviors can now be used to compare two objects on update

# Version 0.1.7

* BehaviorManager: map behaviors via game object id to prevent race conditions
* introduced DEBUG logging for GameWorld
* rewrote the ID system - now a UUID is being used
* GameObject: added internal id which does not change, even after re-obtaining from the pool
* GameObject: added previous id from a previous pooling iteration

# Version 0.1.6

* GameCamera: add 'focus(GameObject)' method
* bugfix: remove objects which are outside of the world, even if they are inactive

# Version 0.1.5

* do not set center on sprite rotation


# Version 0.1.4

* added rotation to SpriteRenderer

# Version 0.1.3

* added rotation attribute to game objects
* add 'scale' method to game objects
* introduced lazy behavior creation/deletion

# Version 0.1.2

* added partial support for viewports
* introduced lazy object creation for game worlds

# Version 0.1.1

Added offset attribute to `AnimationRenderer` class.

# Version 0.1.0

This version improves braingdx and comes with the following highlights:

* [[#84](https://github.com/bitbrain/braingdx/issues/84)] introduced new UI stage which works on the world render level
* [[#83](https://github.com/bitbrain/braingdx/issues/83)] game objects can now be retrieved via their respective ID
* [[#76](https://github.com/bitbrain/braingdx/issues/76)] added various shader tweens
* [[#79](https://github.com/bitbrain/braingdx/issues/79)] added a new assetloader which uses reflection to load assets
* [[#81](https://github.com/bitbrain/braingdx/issues/81)] introduce mutator pattern for game object initialisation

# Version 0.0.1

* render pipeline
* lighting pipeline
* basic behaviors
