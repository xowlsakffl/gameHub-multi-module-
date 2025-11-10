import Sidebar from "../components/layout/Sidebar.jsx";
import Header from "../components/layout/Header.jsx";
import MainContent from "../components/layout/MainContent.jsx";
import FriendList from "../components/layout/FriendList.jsx";

export default function Home() {
    return (
        <div className="flex h-screen bg-onion-background text-[var(--color-onion-text)] overflow-hidden">
            {/* 좌측 사이드바 */}
            <Sidebar />

            {/* 중앙 메인 레이아웃 */}
            <div className="flex flex-col flex-1 overflow-hidden">
                {/* 상단 헤더 */}
                <Header />

                {/* 중앙 콘텐츠 + 우측 친구목록 */}
                <div className="flex flex-1 overflow-hidden">
                    {/* 메인 콘텐츠 영역 */}
                    <div className="flex-1 overflow-y-auto">
                        <MainContent />
                    </div>

                    {/* 우측 친구목록 */}
                    <FriendList />
                </div>
            </div>
        </div>
    );
}
