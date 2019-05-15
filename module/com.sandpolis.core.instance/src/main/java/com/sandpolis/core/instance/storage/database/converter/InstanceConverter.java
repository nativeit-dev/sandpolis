/******************************************************************************
 *                                                                            *
 *                    Copyright 2018 Subterranean Security                    *
 *                                                                            *
 *  Licensed under the Apache License, Version 2.0 (the "License");           *
 *  you may not use this file except in compliance with the License.          *
 *  You may obtain a copy of the License at                                   *
 *                                                                            *
 *      http://www.apache.org/licenses/LICENSE-2.0                            *
 *                                                                            *
 *  Unless required by applicable law or agreed to in writing, software       *
 *  distributed under the License is distributed on an "AS IS" BASIS,         *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *
 *  See the License for the specific language governing permissions and       *
 *  limitations under the License.                                            *
 *                                                                            *
 *****************************************************************************/
package com.sandpolis.core.instance.storage.database.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.sandpolis.core.proto.util.Platform.Instance;

/**
 * When used on a persistent {@link Instance} field, this converter makes
 * database operations much more efficient by replacing {@link Instance}s with
 * their numeric identifier.
 * 
 * @author cilki
 * @since 5.0.0
 */
@Converter
public class InstanceConverter implements AttributeConverter<Instance, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Instance instance) {
		return instance.getNumber();
	}

	@Override
	public Instance convertToEntityAttribute(Integer dbData) {
		return Instance.forNumber(dbData);
	}

}