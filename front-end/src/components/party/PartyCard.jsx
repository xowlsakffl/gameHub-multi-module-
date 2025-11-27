export default function PartyCard({ party }) {
    return (
        <div className="bg-onion-surface border border-onion-border rounded-xl p-4 shadow-md hover:shadow-lg hover:border-onion-primary transition-all duration-200">
            <h3 className="text-lg font-bold text-white mb-2">{party.title}</h3>
            <p className="text-sm text-gray-400 mb-1">🎮 {party.game}</p>
            <p className="text-sm text-gray-400 mb-3">
                👥 {party.currentMembers}/{party.maxMembers}명
            </p>
            <p className="text-sm text-gray-500">호스트: {party.host}</p>
        </div>
    );
}
