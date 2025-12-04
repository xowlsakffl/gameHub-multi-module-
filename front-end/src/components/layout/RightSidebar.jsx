import { FaUserFriends, FaComments, FaPlus } from "react-icons/fa";
import { Tooltip } from "react-tooltip";

export default function RightSidebar({ friendOpen, setFriendOpen }) {
    return (
        <div
            className="
                fixed right-0
                top-16      /* ← 헤더 높이 만큼 내려줌 */
                h-[calc(100%-64px)]  /* ← 전체 높이 - 헤더 높이 */
                w-20
                flex flex-col items-center
                bg-onion-surface border-l border-onion-border
                py-4 space-y-4
                z-30
            "
        >


        {/* 메뉴 아이콘 목록 */}
            <div className="flex flex-col items-center space-y-3 flex-1">

                {/* 친구 목록 아이콘 */}
                <button
                    data-tooltip-id="tip-friends"
                    data-tooltip-content="친구 목록"
                    className={`
                        w-12 h-12 flex items-center justify-center rounded-xl text-xl
                        transition-all duration-200
                        ${friendOpen
                        ? "bg-onion-primary text-white"
                        : "text-gray-400 hover:text-white hover:bg-onion-input"
                    }
                    `}
                    onClick={() => setFriendOpen(!friendOpen)}
                >
                    <FaUserFriends />
                    <Tooltip id="tip-friends" place="left" />
                </button>

                {/* DM 아이콘 */}
                <button
                    data-tooltip-id="tip-dm"
                    data-tooltip-content="DM 목록"
                    className="
                        w-12 h-12 flex items-center justify-center rounded-xl text-xl
                        text-gray-400 hover:text-white hover:bg-onion-input transition-all
                    "
                >
                    <FaComments />
                    <Tooltip id="tip-dm" place="left" />
                </button>
            </div>
        </div>
    );
}
