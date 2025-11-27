export default function FriendList() {
    const friends = [
        { name: "민성", status: "online" },
        { name: "지현", status: "online" },
        { name: "유진", status: "offline" },
    ];

    return (
        <div className="w-72 bg-onion-surface border-l border-onion-border p-4 hidden md:flex flex-col">
            <h2 className="text-lg font-semibold text-white mb-4">친구 목록</h2>
            <div className="space-y-3">
                {friends.map((f) => (
                    <div
                        key={f.name}
                        className="flex items-center justify-between text-gray-300"
                    >
                        <div className="flex items-center gap-2">
                            <div
                                className={`w-2.5 h-2.5 rounded-full ${
                                    f.status === "online" ? "bg-green-400" : "bg-gray-500"
                                }`}
                            />
                            <span>{f.name}</span>
                        </div>
                        <button className="text-xs text-onion-primary hover:underline">
                            DM
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
}
