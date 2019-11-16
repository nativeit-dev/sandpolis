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
package com.sandpolis.server.vanilla.gen.mega;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.stream.Collectors;

import com.github.cilki.zipset.ZipSet;
import com.github.cilki.zipset.ZipSet.EntryPath;
import com.sandpolis.core.instance.Core;
import com.sandpolis.core.instance.Environment;
import com.sandpolis.core.util.ArtifactUtil;
import com.sandpolis.server.vanilla.gen.MegaGen;

import com.sandpolis.core.proto.util.Platform.OsType;
import com.sandpolis.core.proto.util.Generator.GenConfig;

/**
 * This generator produces a runnable jar file.
 *
 * @author cilki
 * @since 5.0.0
 */
public class JarPackager extends MegaGen {
	public JarPackager(GenConfig config) {
		super(config, ".jar", "/lib/sandpolis-client-installer.jar");
	}

	@Override
	protected byte[] generate() throws Exception {
		Path client = Environment.LIB.path().resolve("sandpolis-client-mega-" + Core.SO_BUILD.getVersion() + ".jar");

		ZipSet output;
		if (config.getMega().getMemory()) {
			output = new ZipSet(client);

			// Add client configuration
			output.add("soi/client.bin", config.getMega().toByteArray());

			for (String gav : getDependencies()) {
				String filename = String.format("%s-%s.jar", gav.split(":")[1], gav.split(":")[2]);

				// TODO merge
			}
		} else {
			Properties cfg = buildInstallerConfig();
			cfg.setProperty("screen.session", "com.sandpolis.client.mega");

			output = new ZipSet(readArtifactBinary());

			// Add installer configuration
			try (var out = new ByteArrayOutputStream()) {
				cfg.store(out, null);

				output.add("config.properties", out.toByteArray());
			}

			if (!config.getMega().getDownloader()) {
				for (String dependency : getDependencies()) {
					Path source = ArtifactUtil.getArtifactFile(Environment.LIB.path(), dependency);

					if (Files.exists(source)) {
						// Add library
						output.add("lib/" + source.getFileName(), source);
					} else {
						throw new FileNotFoundException(source.toString());
					}
				}
			}

			// Add client configuration
			output.add(EntryPath.get("lib/" + client.getFileName(), "soi/client.bin"), config.getMega().toByteArray());
		}

		return output.build();
	}
}