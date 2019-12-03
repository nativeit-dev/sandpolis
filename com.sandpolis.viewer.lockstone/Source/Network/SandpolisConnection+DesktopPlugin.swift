//****************************************************************************//
//                                                                            //
//                Copyright © 2015 - 2019 Subterranean Security               //
//                                                                            //
//  Licensed under the Apache License, Version 2.0 (the "License");           //
//  you may not use this file except in compliance with the License.          //
//  You may obtain a copy of the License at                                   //
//                                                                            //
//      http://www.apache.org/licenses/LICENSE-2.0                            //
//                                                                            //
//  Unless required by applicable law or agreed to in writing, software       //
//  distributed under the License is distributed on an "AS IS" BASIS,         //
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  //
//  See the License for the specific language governing permissions and       //
//  limitations under the License.                                            //
//                                                                            //
//****************************************************************************//
import Foundation
import NIO
import SwiftProtobuf
import os

extension SandpolisConnection {

	/// Request a screenshot from the given client.
	///
	/// - Parameter target: The target client's CVID
	/// - Returns: A response future
	func screenshot(_ target: Int32) -> EventLoopFuture<Any> {
		var rq = Net_MSG.with {
			$0.to = target
			$0.plugin = try! Google_Protobuf_Any(message: Net_DesktopMSG.with {
				$0.rqScreenshot = Net_RQ_Screenshot()
			}, typePrefix: "com.sandpolis.plugin.desktop")
		}

		os_log("Requesting screenshot for client: %d", target)
		return request(&rq).map { rs in
			do {
				return try Net_DesktopMSG.init(unpackingAny: rs.plugin)
			} catch {
				return rs.rsOutcome
			}
		}
	}

	/// Request a new remote desktop session from the given client.
	///
	/// - Parameter target: The target client's CVID
	/// - Parameter receiver: The controller to receive events
	/// - Returns: The stream
	func remote_desktop(_ target: Int32, _ receiver: RemoteDesktop) -> SandpolisStream {
		let stream = SandpolisStream(self, SandpolisUtil.stream())
		stream.register { (m: Net_MSG) -> Void in
			receiver.onEvent(try! Net_DesktopMSG.init(unpackingAny: m.plugin).evDesktopStream)
		}
		streams.append(stream)

		var rq = Net_MSG.with {
			$0.to = target
			$0.plugin = try! Google_Protobuf_Any(message: Net_DesktopMSG.with {
				$0.rqDesktopStream = Net_RQ_DesktopStream.with {
					$0.id = stream.id
				}
			}, typePrefix: "com.sandpolis.plugin.desktop")
		}

		os_log("Requesting remote desktop session for client: %d", target)
		_ = request(&rq)
		return stream
	}
}
