/*******************************************************************************
 *                                                                             *
 *                Copyright © 2015 - 2019 Subterranean Security                *
 *                                                                             *
 *  Licensed under the Apache License, Version 2.0 (the "License");            *
 *  you may not use this file except in compliance with the License.           *
 *  You may obtain a copy of the License at                                    *
 *                                                                             *
 *      http://www.apache.org/licenses/LICENSE-2.0                             *
 *                                                                             *
 *  Unless required by applicable law or agreed to in writing, software        *
 *  distributed under the License is distributed on an "AS IS" BASIS,          *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 *  See the License for the specific language governing permissions and        *
 *  limitations under the License.                                             *
 *                                                                             *
 ******************************************************************************/
package com.sandpolis.server.vanilla.store.location;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableBiMap;
import com.sandpolis.core.proto.util.LocationOuterClass.Location;

import java.util.Set;

public class KeyCdn extends AbstractGeolocationService {

	private static final ImmutableBiMap<Integer, String> attrMap = new ImmutableBiMap.Builder<Integer, String>()
			.put(Location.AS_CODE_FIELD_NUMBER, "asn").put(Location.CITY_FIELD_NUMBER, "city")
			.put(Location.CONTINENT_FIELD_NUMBER, "continent_name")
			.put(Location.CONTINENT_CODE_FIELD_NUMBER, "continent_code")
			.put(Location.COUNTRY_FIELD_NUMBER, "country_name").put(Location.COUNTRY_CODE_FIELD_NUMBER, "country_code")
			.put(Location.ISP_FIELD_NUMBER, "isp").put(Location.LATITUDE_FIELD_NUMBER, "latitude")
			.put(Location.LONGITUDE_FIELD_NUMBER, "lonitude").put(Location.METRO_CODE_FIELD_NUMBER, "metro_code")
			.put(Location.POSTAL_CODE_FIELD_NUMBER, "postal_code").put(Location.REGION_FIELD_NUMBER, "region_name")
			.put(Location.REGION_CODE_FIELD_NUMBER, "region_code").put(Location.TIMEZONE_FIELD_NUMBER, "timezone")
			.build();

	public KeyCdn() {
		super(attrMap, "https");
	}

	@Override
	protected String buildQuery(String ip, Set<Integer> fields) {
		return String.format("%s://tools.keycdn.com/geo.json?host=%s", protocol, ip);
	}

	@Override
	protected Location parseLocation(String result) throws Exception {
		var location = Location.newBuilder();
		new ObjectMapper().readTree(result).path("data").path("geo").forEach(node -> {
			node.fields().forEachRemaining(entry -> {
				var field = Location.getDescriptor().findFieldByNumber(attrMap.inverse().get(entry.getKey()));
				switch (field.getNumber()) {
				case Location.LATITUDE_FIELD_NUMBER:
				case Location.LONGITUDE_FIELD_NUMBER:
					// Set double value
					location.setField(field, entry.getValue().asDouble());
					break;
				case Location.POSTAL_CODE_FIELD_NUMBER:
					// Set int value
					location.setField(field, entry.getValue().asInt());
					break;
				default:
					// Set string value
					location.setField(field, entry.getValue().asText());
					break;
				}
			});
		});

		return location.build();
	}
}