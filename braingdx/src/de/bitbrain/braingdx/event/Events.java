/* Copyright 2016 Miguel Gonzalez Sanchez
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

package de.bitbrain.braingdx.event;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.common.Properties;
import net.engio.mbassy.bus.config.BusConfiguration;
import net.engio.mbassy.bus.config.ConfigurationError;
import net.engio.mbassy.bus.config.ConfigurationErrorHandler;
import net.engio.mbassy.bus.config.Feature;
import net.engio.mbassy.bus.error.IPublicationErrorHandler;
import net.engio.mbassy.bus.error.PublicationError;

/**
 * Singleton which is able to fire events of any kind
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
public final class Events {

    private static final Events INSTANCE = new Events();

    private MBassador bus;

    private Events() {
        bus = new MBassador((new BusConfiguration()
                .addFeature(Feature.SyncPubSub.Default())
                .addFeature(Feature.AsynchronousHandlerInvocation.Default())
                .addFeature(Feature.AsynchronousMessageDispatch.Default())
                .addConfigurationErrorHandler(new ConfigurationErrorHandler() {
                    @Override
                    public void handle(ConfigurationError error) {
                        // noOp
                    }
                })
        .setProperty(Properties.Common.Id, "global bus")));
    }

    public static Events getInstance() {
        return INSTANCE;
    }

    public void fire(String type, Object primary, Object ... secondaries) {
        bus.publish(new GdxEvent(type, primary, secondaries));
    }

    public void register(Object object) {
        bus.subscribe(object);
    }

    public void unregister(Object object) {
        bus.unsubscribe(object);
    }

    public static class GdxEvent {

        private Object primary;

        private Object[] secondaries;

        private String type;

        private GdxEvent(String type, Object primaryParameter, Object ... secondaryParameters) {
            this.type = type;
            this.primary = primaryParameter;
            this.secondaries = secondaryParameters;
        }

        public boolean isTypeOf(String type) {
            return this.type.equals(type);
        }

        public Object getPrimaryParam() {
            return primary;
        }

        public boolean hasPrimaryParam() {
            return primary != null;
        }

        public Object getSecondaryParam(int index) {
           if (hasSecondaryParam(index)) {
               return secondaries[index];
           } else {
               return null;
           }
        }

        public boolean hasSecondaryParams() {
            return secondaries != null && secondaries.length > 0;
        }

        public boolean hasSecondaryParam(int index) {
            return hasSecondaryParams() && index >= 0 && index < secondaries.length;
        }
    }
}
