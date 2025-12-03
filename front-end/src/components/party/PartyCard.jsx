export default function PartyCard({ party }) {

    /** 타입별 뱃지 */
    const getBadge = () => {
        if (party.type === "REQUEST_JOIN") {
            return (
                <span className="absolute top-3 right-3 px-3 py-1 bg-amber-500 text-black text-xs font-semibold rounded-full shadow">
                    승인제
                </span>
            );
        }
        if (party.type === "AUTO_JOIN") {
            return (
                <span className="absolute top-3 right-3 px-3 py-1 bg-green-500 text-black text-xs font-semibold rounded-full shadow">
                    자동참가
                </span>
            );
        }
        return null;
    };

    /** 타입별 버튼 */
    const getActionButton = () => {
        if (party.type === "AUTO_JOIN") {
            return (
                <button
                    className="px-3 py-2 bg-green-600 hover:bg-green-500 text-xs sm:text-sm text-white rounded-lg shadow transition"
                >
                    참가하기
                </button>
            );
        }

        if (party.type === "REQUEST_JOIN") {
            return (
                <button
                    className="px-3 py-2 bg-onion-primary hover:bg-onion-primary-hover text-xs sm:text-sm text-white rounded-lg shadow transition"
                >
                    참가 신청
                </button>
            );
        }

        return null;
    };

    return (
        <div className="relative bg-onion-surface border border-onion-border rounded-xl p-4 shadow-md hover:shadow-lg hover:border-onion-primary transition-all duration-200">

            {/* 뱃지 */}
            {getBadge()}

            {/* 제목 */}
            <h3 className="text-lg font-bold text-white mb-2 pr-16">
                {party.title}
            </h3>

            {/* 설명 */}
            {party.description && (
                <p className="text-sm text-gray-300 mb-3 line-clamp-2">
                    {party.description}
                </p>
            )}

            {/* 게임명 */}
            <p className="text-sm text-gray-400 mb-1">
                🎮 {party.gameName}
            </p>

            {/* 인원 */}
            <p className="text-sm text-gray-400 mb-8">
                👥 {party.currentPlayers}/{party.maxPlayer}명
            </p>

            {/* 호스트 */}
            <p className="text-sm text-gray-500">
                호스트: {party.creator}
            </p>

            {/* 오른쪽 하단 버튼 */}
            <div className="absolute bottom-3 right-3">
                {getActionButton()}
            </div>
        </div>
    );
}
