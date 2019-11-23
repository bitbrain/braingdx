# UNRELEASED 0.6.2

* [[#203](https://github.com/bitbrain/braingdx/issues/203)] make loading of tiled map more consistent
* [[#204](https://github.com/bitbrain/braingdx/issues/204)] return already loaded context when trying to load again
* [[#205](https://github.com/bitbrain/braingdx/issues/205)] add support for registering updateables to the game context
* [[#206](https://github.com/bitbrain/braingdx/issues/206)] inject map properties directly as attributes
# Version 0.6.2

Fix rotations for physics bodies.

# Version 0.6.1

Fixed a small issue which caused physics to get positioned wrongly.

# Version 0.6.0

> **Disclaimer!** this version introduces major API refactoring and will produce compilation errors before migration!

This update changes the entire architecture of this framework by separating reponsibilities into submodules:

- **braingdx-core** the core dependency now contains only generic core logic and boilerplate
- **braingdx-2d** the 2D dependency provides features such as 2D lighting and 2D physics support as well as tiledmap support

Later on, a new **braingdx-3d** module will be added which provides a different kind of feature set such as Blender support.

## Features

* [[#95](https://github.com/bitbrain/braingdx/issues/95)] added pause game functionality
* [[#144](https://github.com/bitbrain/braingdx/issues/144)] replace `TiledMapAPI` with `TiledMapContext`
* [[#186](https://github.com/bitbrain/braingdx/issues/186)] drastically improved performance on mobile devices
* [[#193](https://github.com/bitbrain/braingdx/issues/193)] added color manipulation utility
* [[#194](https://github.com/bitbrain/braingdx/issues/194)] added point light tween
* [[#202](https://github.com/bitbrain/braingdx/issues/202)] infer MapType from TiledMap orientation property
 
## Bugfixes
 
* [[#103](https://github.com/bitbrain/braingdx/issues/103)] resolved an issue where global behaviours could not be loaded globally within a loop
* [[#197](https://github.com/bitbrain/braingdx/issues/197)] drastically reduce memory footprint
* [[#199](https://github.com/bitbrain/braingdx/issues/199)] fixing camera position setting

# Version 0.5.17

* [[#98](https://github.com/bitbrain/braingdx/issues/98)] only remove relevant objects from the world when disposing the tiled manager

# Version 0.5.16

* remove `Iterable` interface from `GameWorld`
* add new `Group` utility
* [[#96](https://github.com/bitbrain/braingdx/issues/96)] introduce game object groups accessible via `GameWorld`

# Version 0.5.15

* reduce memory footprint by only calling `GameObject.toString()` when debug level is active

# Version 0.5.14

Further improvements on memory heap footprint:

* remove `BigDecimalVector2`
* add `DoubleVector2`
* replace `BigDecimal` with `double` for camera tracking and camera alignment

# Version 0.5.13

* only create animation states dynamically whenever they are required
* only compute tmx layer indices whenever they are required

# Version 0.5.12

* only create `AStarPathFinder` when it is actually used to reduce memory footprint
* significantly reduce memory footprint of `TiledMapManager` by dynamically creating cell states at runtime 
* implement `toString()` method of `CombinedRenderPipe`

# Version 0.5.11

* add new `particleMultiplier` to `GraphicsSettings` in order to dynamically control the amount of all particles

# Version 0.5.10

* add additional constructor to `StringRandomizer` in order to provide a fixed seed

# Version 0.5.9

This version introduces string manipulation with additional tween engine support:

* [[#196](https://github.com/bitbrain/braingdx/issues/196)] implement `StringRandomizer`
* implement `StringRandomizerTween`

# Version 0.5.8

* add new `getObjects` method to `GameWorld`

# Version 0.5.7

* deleted `GameInputAdaptr` - use `GestureListener` via libgdx instead
* implemented additional input variations for `Navigateable`

# Version 0.5.6

* [[#190](https://github.com/bitbrain/braingdx/issues/190)] fix a crash due to box2dlights ray handler not being disposed

# Version 0.5.5

* [[#187](https://github.com/bitbrain/braingdx/issues/187)] do not create frame buffers when no shaders are being used
* removed `RenderPipeline.getPipe` method
* added `RenderPipeline.addEffects` method
* added `RenderPipe.hasEffects` method

# Version 0.5.4

* [[#185](https://github.com/bitbrain/braingdx/issues/185)] added new `setDistanceStoppingThreshold` method to `GameCamera` which allows defining a threshold to avoid flickering
* added a new overloaded `setAmbientLight` method to `LightingManager` which allows fading the ambient light

# Version 0.5.3

* added sound styles to `NavigationMenu`

# Version 0.5.2

* fixed an issue where behaviours would not get removed on attached particle effects which are not continuous.

# Version 0.5.1

* [[#184](https://github.com/bitbrain/braingdx/issues/184)] implement concept of vertical and horizontal camera speed

# Version 0.5.0

**The physics update** - this update introduces a brand new`PhysicsManager` which Box2D integration out-of-the-box. Additionally,
this update fixes some older bugs as well, such as particle emission issues.

* [[#70](https://github.com/bitbrain/braingdx/issues/70)] integrate Box2D support via `PhysicsManager` accessible via `GameContext`
* [[#131](https://github.com/bitbrain/braingdx/issues/131)] fix `IllegalStateException` when window size is scaled down to 0
* [[#152](https://github.com/bitbrain/braingdx/issues/152)] extend `RenderPipeline` API by new methods
* [[#153](https://github.com/bitbrain/braingdx/issues/153)] fix an issue where particle effects where emitted twice
* [[#182](https://github.com/bitbrain/braingdx/issues/182)] fix collision bug within `SimpleWorldBounds`

# Version 0.4.11

* improve API around `NavigationMenu`

# Version 0.4.10

* [[#178](https://github.com/bitbrain/braingdx/issues/178)] add `shake()` method to `GameCamera` to shake the screen
* [[#179](https://github.com/bitbrain/braingdx/issues/179)] implement `NavigationMenu` class, including controller and keyboard handlers
* [[#180](https://github.com/bitbrain/braingdx/issues/180)] add controller support to `InputManager`
* cleaning up `ActorTween` API

# Version 0.4.9

* [[#175](https://github.com/bitbrain/braingdx/issues/175)] remove `MovementController` API
* [[#176](https://github.com/bitbrain/braingdx/issues/176)] improve deferred loading and unloading of behaviors and game objects
* [[#177](https://github.com/bitbrain/braingdx/issues/177)] introduce new `Updateable` interface which can be applied on `Behavior` and `InputProcessor` implementations

# Version 0.4.8

* add `setAlpha` to `AnimationDrawable`

# Version 0.4.7

* add offset to `AnimationRenderer`

# Version 0.4.6

* add new utility class `GameObjectUtils`

# Version 0.4.5

* `spawnEffect` now returns an `ParticleEffect` object respectively

# Version 0.4.4.1

* fix a bug within `ColouredRenderLayer` so it resizes correctly

# Version 0.4.4

* [[#172](https://github.com/bitbrain/braingdx/issues/172)] fix `NullPointerException` in `GameEventRouter`
* removed `internalId` and `previousId` from `GameObject`

# Version 0.4.3

* [[#171](https://github.com/bitbrain/braingdx/issues/171)] make animation API independent of `GameObject` class
* [[#170](https://github.com/bitbrain/braingdx/issues/170)] introduce `AnimationDrawable` class

# Version 0.4.2

* [[#168](https://github.com/bitbrain/braingdx/issues/168)] introduce scaling by `GameObject` origin
* changed method signatures of `GameObject` getters and added additional getters
* `AnimationRenderer` now uses `Sprite` to render `TextureRegion` frames

# Version 0.4.1

* fix rendering of animations on negative scaling

# Version 0.4.0

* [[#46](https://github.com/bitbrain/braingdx/issues/46)] rewrote the entire animation API. It is now much more straight forward to define animations within **braingdx**

# Version 0.3.8

* [[#161](https://github.com/bitbrain/braingdx/issues/161)] add `GameObject.removeAttribute` method. Additionally, `setAttribute(key, null)` will remove the attribute entirely now.

# Version 0.3.7

* [[#159](https://github.com/bitbrain/braingdx/issues/159)] origin of `Zoomer` now applies correctly independent of render scale

# Version 0.3.6

* [[#158](https://github.com/bitbrain/braingdx/issues/158)] simplify shader manager API

# Version 0.3.5

* `ColorTransition` now properly applies custom colors
* [[#135](https://github.com/bitbrain/braingdx/issues/135)] implement `GameInputAdapter`
* [[#155](https://github.com/bitbrain/braingdx/issues/155)] introduced `Settings` and `GraphicsSettings`
* [[#156](https://github.com/bitbrain/braingdx/issues/156)] created `GameInputAdapter` for better input handling
* [[#157](https://github.com/bitbrain/braingdx/issues/157)] added `ShaderManager`

# Version 0.3.4.1

* add `GameCameraTween`

# Version 0.3.4

* [[#132](https://github.com/bitbrain/braingdx/issues/132)] fix `NumberFormatException` in `VectorGameCamera`
* [[#153](https://github.com/bitbrain/braingdx/issues/153)] do not reset the particle emitter before `start()`

# Version 0.3.3

* [[#107](https://github.com/bitbrain/braingdx/issues/107)] introduce TiledMap animations

# Version 0.3.2

* [[#151](https://github.com/bitbrain/braingdx/issues/151)] `GameObject` scaling does now affect animation rendering 

# Version 0.3.1

* [[#150](https://github.com/bitbrain/braingdx/issues/150)] changing world bounds should now reset camera stickiness 

# Version 0.3.0

This release brings major improvements to the `GameCamera` logic and API within **braingdx**.

* [[#102](https://github.com/bitbrain/braingdx/issues/129)] fixing stickiness of `GameCamera`
* [[#129](https://github.com/bitbrain/braingdx/issues/129)] setting game camera attributes works now without a target attached
* [[#145](https://github.com/bitbrain/braingdx/issues/145)] improve `GameCamera` API with additional methods to retrieve dimensions and position
* [[#146](https://github.com/bitbrain/braingdx/issues/146)] introduced GWT math
* [[#147](https://github.com/bitbrain/braingdx/issues/147)] introduced `BigDecimalVector2` math component

**Other fixes**

* improve precision of `GameCamera` object tracking

# Version 0.2.17

* [[#140](https://github.com/bitbrain/braingdx/issues/140)] fix issue where tiled collisions were overriden and removed by other game objects
* [[#141](https://github.com/bitbrain/braingdx/issues/141)] improve collision API for tiled maps. Introduce `isExclusiveCollision` and `isInclusiveCollision` methods to `TiledMapAPI`

# Version 0.2.16

This release fixes collision removal for tiled maps.

* [[#136](https://github.com/bitbrain/braingdx/issues/136)] fix issue where moving game objects leave collisions behind
* add new method to `GameObject::getOrSetAttribute` which retrieves or sets an attribute, depending on its existence

# Version 0.2.15

This release fixes various issues around tiled map support within **braingdx**.

* [[#133](https://github.com/bitbrain/braingdx/issues/133)] Introduce `TiledMapAPI.setEventFactory(GameEventFactory)`
* [[#135](https://github.com/bitbrain/braingdx/issues/135)] Game objects with a collision now have an indexed initial position
* fixed a bug where sticky events would be accidentally deleted
* added a `GameEventRouter` class which publishes game events when game objects collide with event objects

# Version 0.2.14

* [[#134](https://github.com/bitbrain/braingdx/issues/134)] extend TiledCollisionResolver

# Version 0.2.13

* [[#127](https://github.com/bitbrain/braingdx/issues/127)] fix initial project setup

# Version 0.2.12

* [[#118](https://github.com/bitbrain/braingdx/issues/118)] replace listeners with TiledMap events
* do not log an error message when no event listeners are defined for a published event

# Version 0.2.11.2

* introduce **box2dlights** 1.5-SNAPSHOT

# Version 0.2.11.1

* [[#124](https://github.com/bitbrain/braingdx/issues/124)] introduce new deployment flow

# Version 0.2.11

* [[#123](https://github.com/bitbrain/braingdx/issues/123)] introduce Maven Central only-dependencies

# Version 0.2.10

* [[#113](https://github.com/bitbrain/braingdx/issues/113)] implement advanced collision detection for larger object
* [[#120](https://github.com/bitbrain/braingdx/issues/120)] add fingerprints to collisions
* [[#121](https://github.com/bitbrain/braingdx/issues/121)] include object collisions for path finding

# Version 0.2.9

* add method to `AudioManager` to directly attach sounds to game objects

# Version 0.2.8

* [[#119](https://github.com/bitbrain/braingdx/issues/119)] hide invisible tiledmap layers

# Version 0.2.7

* [[#116](https://github.com/bitbrain/braingdx/issues/116)] set object color correctly on animations
* [[#117](https://github.com/bitbrain/braingdx/issues/117)] add support to TMX to disable collisions for game objects

# Version 0.2.6

* [[#114](https://github.com/bitbrain/braingdx/issues/114)] integrate path finder
* [[#115](https://github.com/bitbrain/braingdx/issues/115)] improved particle management
* fix a bug where `onEnterCell` had been called before collisions were updated

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
