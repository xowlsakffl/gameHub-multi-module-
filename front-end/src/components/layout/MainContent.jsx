import PartyCard from "../party/PartyCard.jsx";

export default function MainContent() {
    const mockParties = [
        {
            id: 1,
            title: "랭크 듀오 구함",
            game: "League of Legends",
            currentMembers: 2,
            maxMembers: 5,
            host: "민성",
        },
        {
            id: 2,
            title: "클랜전 준비중",
            game: "PUBG",
            currentMembers: 4,
            maxMembers: 10,
            host: "지현",
        },
        {
            id: 3,
            title: "5인팟 같이할 사람!",
            game: "Overwatch 2",
            currentMembers: 3,
            maxMembers: 5,
            host: "유진",
        },
    ];

    return (
        <div className="p-6">
            <h2 className="text-2xl font-semibold mb-4 text-white">
                🔥 현재 오픈된 파티
            </h2>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
                {mockParties.map((party) => (
                    <PartyCard key={party.id} party={party} />
                ))}
            </div>
        </div>
    );
}
