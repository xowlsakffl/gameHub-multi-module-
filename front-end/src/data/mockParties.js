const mockPartySeed = [
    {
        title: "발로란트 경쟁전 실버-골드 듀오 구해요",
        description: "브리치, 스카이 가능하신 분 우대. 디코 필수, 분위기 좋게 3판만 빠르게 갑니다.",
        gameName: "VALORANT",
        currentPlayers: 3,
        maxPlayer: 5,
        creator: "헤드샷민수",
        type: "REQUEST_JOIN",
    },
    {
        title: "롤 자랭 5인큐 탑/정글 모집",
        description: "현 플래-에메 구간, 오더 가능하신 분 환영. 채팅 과한 분은 바로 정리합니다.",
        gameName: "리그 오브 레전드",
        currentPlayers: 3,
        maxPlayer: 5,
        creator: "바텀차이",
        type: "AUTO_JOIN",
    },
    {
        title: "오버워치2 경쟁전 2힐 1딜 구함",
        description: "실버-플레 구간, 마이크 on. 탱은 라인/시그마 위주로 갑니다.",
        gameName: "오버워치 2",
        currentPlayers: 2,
        maxPlayer: 5,
        creator: "힐밴",
        type: "REQUEST_JOIN",
    },
    {
        title: "배그 스쿼드 치킨각 보는 분만",
        description: "에란겔 위주 운영, 초반 핫드랍 안 합니다. 핑 잘 보시는 분.",
        gameName: "PUBG: 배틀그라운드",
        currentPlayers: 2,
        maxPlayer: 4,
        creator: "치킨남매",
        type: "AUTO_JOIN",
    },
    {
        title: "FC온라인 2:2 친선 하실 분",
        description: "티키타카 맞춰볼 분, 채팅 매너 좋으신 분 선호합니다.",
        gameName: "FC 온라인",
        currentPlayers: 2,
        maxPlayer: 4,
        creator: "압박축구",
        type: "AUTO_JOIN",
    },
    {
        title: "로스트아크 카멘 노말 1-3 트라이",
        description: "기믹 공부하고 오신 분만. 실수 괜찮지만 반복 실수는 피드백 드립니다.",
        gameName: "로스트아크",
        currentPlayers: 6,
        maxPlayer: 8,
        creator: "에스더각",
        type: "REQUEST_JOIN",
    },
    {
        title: "메이플 보스돌이 길드원 구함",
        description: "주간보스 같이 돌고 정보 공유하실 분, 디스코드 공지 확인 가능해야 합니다.",
        gameName: "메이플스토리",
        currentPlayers: 8,
        maxPlayer: 12,
        creator: "검마연습생",
        type: "AUTO_JOIN",
    },
    {
        title: "에이펙스 랭크 골드 구간 1명",
        description: "포지션 유동적으로 가능하신 분. 한타 콜 빠르게 맞춰요.",
        gameName: "Apex Legends",
        currentPlayers: 2,
        maxPlayer: 3,
        creator: "점프마스터",
        type: "REQUEST_JOIN",
    },
    {
        title: "데바데 생존자 4인큐 저녁반",
        description: "퍽 자유, 억까에도 멘탈 괜찮으신 분. 10시까지 달립니다.",
        gameName: "Dead by Daylight",
        currentPlayers: 3,
        maxPlayer: 4,
        creator: "발전기요정",
        type: "AUTO_JOIN",
    },
    {
        title: "마인크래프트 바닐라 신규 서버 멤버",
        description: "건축 좋아하시는 분, 장기적으로 같이 하실 분만 받아요.",
        gameName: "Minecraft",
        currentPlayers: 7,
        maxPlayer: 12,
        creator: "곡괭이맛집",
        type: "REQUEST_JOIN",
    },
    {
        title: "서든 클랜전 5명 맞춰봅니다",
        description: "오더 가능자, 브리핑 빠른 분. 한두 판 후 자리 고정 예정.",
        gameName: "서든어택",
        currentPlayers: 4,
        maxPlayer: 6,
        creator: "헤드라인",
        type: "AUTO_JOIN",
    },
    {
        title: "스타2 협동전 돌변 클리어팟",
        description: "돌변 이해도 있으신 분. 지휘관 자유, 시간 오래 안 끕니다.",
        gameName: "스타크래프트 II",
        currentPlayers: 1,
        maxPlayer: 2,
        creator: "궤도사령부",
        type: "REQUEST_JOIN",
    },
    {
        title: "이터널 리턴 3인 일반 빠르게",
        description: "미리 루트 공유하고 합 맞춰봐요. 피드백 가능한 분 환영.",
        gameName: "이터널 리턴",
        currentPlayers: 2,
        maxPlayer: 3,
        creator: "루미아장인",
        type: "AUTO_JOIN",
    },
    {
        title: "디아4 지옥물결 파밍반",
        description: "시즌 캐릭 위주, 속도감 있게 파밍합니다. 마이크 자유.",
        gameName: "디아블로 IV",
        currentPlayers: 3,
        maxPlayer: 4,
        creator: "성채파괴자",
        type: "AUTO_JOIN",
    },
    {
        title: "레식 랭크 팀원 구합니다",
        description: "공수 브리핑 가능하신 분, 무지성 러시 안 합니다.",
        gameName: "Rainbow Six Siege",
        currentPlayers: 4,
        maxPlayer: 5,
        creator: "드론장인",
        type: "REQUEST_JOIN",
    },
    {
        title: "POE 시즌 스타터 정보 공유방",
        description: "빌드 상담, 맵핑 파티, 거래 팁 같이 나눌 분 모집합니다.",
        gameName: "Path of Exile",
        currentPlayers: 9,
        maxPlayer: 15,
        creator: "카오스오브",
        type: "AUTO_JOIN",
    },
];

const partyVariants = [
    "초보 환영",
    "디코 필수",
    "오더 가능자 우대",
    "매너 플레이",
    "랭크 위주",
    "주말 고정팟",
    "퇴근 후 한두 판",
    "실력보다 텐션",
];

function buildMockParty(seed, index) {
    const currentPlayers = Math.min(
        seed.maxPlayer,
        Math.max(1, seed.currentPlayers + (index % 3) - 1)
    );

    return {
        id: index + 1,
        title: `${seed.title} · ${partyVariants[index % partyVariants.length]}`,
        description: seed.description,
        gameName: seed.gameName,
        currentPlayers,
        maxPlayer: seed.maxPlayer,
        creator: seed.creator,
        type: index % 2 === 0 ? seed.type : seed.type === "AUTO_JOIN" ? "REQUEST_JOIN" : "AUTO_JOIN",
    };
}

export const mockParties = Array.from({ length: 32 }, (_, index) =>
    buildMockParty(mockPartySeed[index % mockPartySeed.length], index)
);

export function getMockPartyPage(page = 0, size = 12) {
    const start = page * size;
    const content = mockParties.slice(start, start + size);

    return {
        content,
        totalPages: Math.ceil(mockParties.length / size),
        totalElements: mockParties.length,
        number: page,
        size,
        isMock: true,
    };
}
