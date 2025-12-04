export default function FriendListPanel({ open, setOpen }) {
    const friends = [
        { name: "민성", status: "online" },
        { name: "지현", status: "online" },
        { name: "유진", status: "offline" },
    ];

    return (
        <div
            className={`
                fixed right-20 
                top-16                /* 헤더 높이만큼 아래로 */
                h-[calc(100%-64px)]   /* 헤더 제외한 나머지 높이 */
                w-72
                bg-onion-surface border-l border-onion-border shadow-xl
                p-4 transform transition-transform duration-300
                z-20
                ${open ? "translate-x-0" : "translate-x-full"}
            `}
        >


        <div className="flex items-center justify-between mb-4">
                <h2 className="text-lg font-semibold text-white">친구 목록</h2>
                <button
                    className="text-gray-400 hover:text-white"
                    onClick={() => setOpen(false)}
                >
                    ✕
                </button>
            </div>

            <div className="space-y-3 overflow-y-auto">
                {friends.map((f) => (
                    <div key={f.name} className="flex items-center justify-between text-gray-300">
                        <div className="flex items-center gap-2">
                            <div
                                className={`w-2.5 h-2.5 rounded-full ${
                                    f.status === "online" ? "bg-green-400" : "bg-gray-500"
                                }`}
                            ></div>
                            <span>{f.name}</span>
                        </div>
                        <button className="text-xs text-onion-primary hover:underline">DM</button>
                    </div>
                ))}
            </div>
        </div>
    );
}
