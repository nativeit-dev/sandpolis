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
package com.sandpolis.core.profile;

import static com.sandpolis.core.instance.store.thread.ThreadStore.ThreadStore;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.sandpolis.core.profile.attribute.key.AttributeKey;
import com.sandpolis.core.proto.net.MsgAttribute.EV_AttributeStream;
import com.sandpolis.core.stream.store.StreamSource;

public class AttributeStreamSource extends StreamSource<EV_AttributeStream> {

	private ScheduledFuture<?> task;

	private long period;

	private List<AttributeKey<?>> keys;

	public AttributeStreamSource(List<String> paths, long period) {
		// TODO
		this.period = period;
	}

	@Override
	public void stop() {
		if (task != null)
			task.cancel(true);
	}

	@Override
	public void start() {
		ScheduledExecutorService service = ThreadStore.get("attributes");
		task = service.scheduleAtFixedRate(() -> {
			submit(EV_AttributeStream.newBuilder().build());
		}, 0, period, TimeUnit.MILLISECONDS);

	}

}