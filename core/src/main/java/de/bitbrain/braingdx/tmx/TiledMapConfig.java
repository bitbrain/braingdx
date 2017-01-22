/* Copyright 2017 Miguel Gonzalez Sanchez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.bitbrain.braingdx.tmx;

/**
 * Contains naming configuration for TMX maps.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public class TiledMapConfig {

    private String collisionAttribute = Constants.COLLISION;
    private String typeAttribute = Constants.TYPE;

    public String getCollisionAttribute() {
	return collisionAttribute;
    }

    public String getTypeAttribute() {
	return typeAttribute;
    }

    public TiledMapConfig collisionAttribute(String attribute) {
	if (attribute != null) {
	    this.collisionAttribute = attribute;
	}
	return this;
    }

    public TiledMapConfig typeAttribute(String attribute) {
	if (attribute != null) {
	    this.typeAttribute = attribute;
	}
	return this;
    }
}
