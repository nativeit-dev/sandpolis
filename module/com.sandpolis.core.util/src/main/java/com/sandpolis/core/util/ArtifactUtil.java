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
package com.sandpolis.core.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.google.common.io.Files;

/**
 * Utilities for managing dependency artifacts and interacting with the Maven
 * Central Repository.
 *
 * @author cilki
 * @since 5.0.0
 */
public final class ArtifactUtil {

	private static final Logger log = LoggerFactory.getLogger(ArtifactUtil.class);

	/**
	 * The central repository base URL.
	 */
	private static final String MAVEN_HOST = "https://repo1.maven.org";

	/**
	 * Get an artifact file from the given directory.
	 *
	 * @param directory   The directory containing artifacts
	 * @param coordinates The artifact's coordinates
	 * @return The artifact's local file
	 */
	public static Path getArtifactFile(Path directory, String coordinates) {
		checkNotNull(directory);
		checkNotNull(coordinates);

		return directory.resolve(ParsedCoordinate.fromCoordinate(coordinates).filename);
	}

	/**
	 * Find all artifacts with the given artifact name. The results will be in
	 * decreasing version order if applicable.
	 * 
	 * @param directory  The directory containing artifacts
	 * @param artifactId The artifact to search for
	 * @return A stream of all matching artifacts
	 * @throws IOException
	 */
	public static Stream<Path> findArtifactFile(Path directory, String artifactId) throws IOException {
		return java.nio.file.Files.list(directory).filter(path -> path.getFileName().toString().startsWith(artifactId))
				// Sort by semantic version number
				.sorted((path1, path2) -> {
					// TODO COMPARE CORRECTLY!
					return ParsedCoordinate.fromFilename(path1.getFileName().toString()).version
							.compareTo(ParsedCoordinate.fromFilename(path2.getFileName().toString()).version);
				});
	}

	/**
	 * Download an artifact from Maven Central to the given directory.
	 *
	 * @param directory The output directory
	 * @param gav       The artifact coordinate in standard Gradle form
	 *                  (group:name:version)
	 * @return The output path
	 * @throws IOException
	 */
	public static Path download(Path directory, String gav) throws IOException {
		checkNotNull(directory);
		checkNotNull(gav);

		var coordinate = ParsedCoordinate.fromCoordinate(gav);
		String url = MAVEN_HOST + "/maven2/" // Base URL
				+ coordinate.groupId.replaceAll("\\.", "/") + "/"// Group name
				+ coordinate.artifactId + "/" // Artifact name
				+ coordinate.version + "/" // Artifact version
				+ coordinate.filename; // Artifact filename
		log.debug("Downloading artifact: {}", url);

		Path output = directory.resolve(coordinate.filename);
		NetUtil.download(url, output.toFile());
		return output;
	}

	/**
	 * Check an artifact's hash.
	 * 
	 * @param directory The artifact directory
	 * @param gav       The artifact coordinate in standard Gradle form
	 *                  (group:name:version)
	 * @return Whether the artifact in the given directory matches the remote hash
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public static boolean checkHash(Path directory, String gav) throws IOException {
		checkNotNull(directory);
		checkNotNull(gav);

		var coordinate = ParsedCoordinate.fromCoordinate(gav);
		String url = MAVEN_HOST + "/maven2/" // Base URL
				+ coordinate.groupId.replaceAll("\\.", "/") + "/"// Group name
				+ coordinate.artifactId + "/" // Artifact name
				+ coordinate.version + "/" // Artifact version
				+ coordinate.filename // Artifact filename
				+ ".sha1";
		log.debug("Downloading hash: {}", url);

		// If the directory does not contain the file, the hash doesn't match
		Path artifact = directory.resolve(coordinate.filename);
		if (!java.nio.file.Files.exists(artifact))
			throw new FileNotFoundException();

		// Download the file hash
		byte[] hash = BaseEncoding.base16().lowerCase().decode(new String(NetUtil.download(url)));

		// Compare hash
		return Arrays.equals(Files.asByteSource(artifact.toFile()).hash(Hashing.sha1()).asBytes(), hash);
	}

	/**
	 * Query the latest version of an artifact.
	 * 
	 * @param gav The artifact coordinate in standard Gradle form
	 *            (group:name:version)
	 * @return The artifact's latest version string
	 * @throws IOException If the connection could not be made, the artifact could
	 *                     not be found, or the metadata could not be parsed
	 */
	public static String getLatestVersion(String gav) throws IOException {
		checkNotNull(gav);

		var coordinate = ParsedCoordinate.fromCoordinate(gav);
		String url = MAVEN_HOST + "/maven2/" // Base URL
				+ coordinate.groupId.replaceAll("\\.", "/") + "/"// Group name
				+ coordinate.artifactId // Artifact name
				+ "/maven-metadata.xml";

		try (var in = new URL(url).openStream()) {
			return XPathFactory.newDefaultInstance().newXPath().evaluate("/metadata/versioning/latest",
					DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in));
		} catch (XPathExpressionException | SAXException | ParserConfigurationException e) {
			throw new IOException(e);
		}
	}

	/**
	 * A container for an artifact's colon-delimited coordinate.
	 */
	public static class ParsedCoordinate {

		public final String coordinate;
		public final String groupId;
		public final String artifactId;
		public final String version;
		public final String filename;

		private ParsedCoordinate(String coordinate, String groupId, String artifactId, String version,
				String filename) {
			this.coordinate = coordinate == null ? "" : coordinate;
			this.groupId = groupId == null ? "" : groupId;
			this.artifactId = artifactId == null ? "" : artifactId;
			this.version = version == null ? "" : version;
			this.filename = filename == null ? "" : filename;
		}

		/**
		 * Parse an artifact's filename.
		 * 
		 * @param filename The standard filename
		 * @return A new {@link ParsedCoordinate}
		 */
		public static ParsedCoordinate fromFilename(String filename) {
			checkNotNull(filename);
			filename = filename.trim();

			String version = filename.substring(filename.lastIndexOf('-') + 1, filename.lastIndexOf(".jar"));
			try {
				String artifact = filename.substring(0, filename.lastIndexOf('-'));
				return new ParsedCoordinate(":" + artifact + ":" + version, null, artifact, version, filename);
			} catch (Exception e) {
				// Missing version
				String artifact = filename.substring(0, filename.lastIndexOf(".jar"));
				return new ParsedCoordinate(":" + artifact + ":", null, artifact, null, filename);
			}
		}

		/**
		 * Parse a coordinate.
		 * 
		 * @param coordinate The coordinate to parse
		 * @return A new {@link ParsedCoordinate}
		 */
		public static ParsedCoordinate fromCoordinate(String coordinate) {
			checkNotNull(coordinate);

			// Hack to produce an empty last element if necessary
			if (coordinate.endsWith(":"))
				coordinate += " ";

			String[] gav = coordinate.split(":");
			checkArgument(gav.length == 3, "Coordinate format: group:artifact:version");

			// Trim fields
			for (int i = 0; i < gav.length; i++)
				gav[i] = gav[i].trim();

			// Build canonical filename
			String filename = gav[1];
			if (!gav[2].isEmpty())
				filename += "-" + gav[2];
			filename += ".jar";

			return new ParsedCoordinate(coordinate.trim(), gav[0], gav[1], gav[2], filename);
		}
	}

	private ArtifactUtil() {
	}
}