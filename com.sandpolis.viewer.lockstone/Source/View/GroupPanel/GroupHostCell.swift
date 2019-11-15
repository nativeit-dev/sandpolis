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
import UIKit

class GroupHostCell: UITableViewCell {

	@IBOutlet weak var platform: UIImageView!
	@IBOutlet weak var hostname: UILabel!

	func setContent(_ profile: SandpolisProfile) {
		hostname.text = profile.hostname
		switch profile.platform {
		case .linux:
			platform.image = UIImage(named: "platform/linux_small")
		case .macos:
			platform.image = UIImage(named: "platform/mac_small")
		case .windows:
			platform.image = UIImage(named: "platform/windows_small")
		case .freebsd:
			platform.image = UIImage(named: "platform/freebsd_small")
		default:
			break
		}
	}
}
