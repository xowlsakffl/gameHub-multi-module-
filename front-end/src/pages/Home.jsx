import { useState } from "react";
import LeftSidebar from "../components/layout/LeftSidebar.jsx";
import Header from "../components/layout/Header.jsx";
import MainContent from "../components/layout/MainContent.jsx";
import RightSidebar from "../components/layout/RightSidebar.jsx";
import FriendListPanel from "../components/layout/FriendListPanel.jsx";

export default function Home() {
    const [friendOpen, setFriendOpen] = useState(false);

    return (
        <div className="flex h-screen bg-onion-background overflow-hidden">
            <LeftSidebar />

            <div className="flex flex-col flex-1 overflow-hidden">
                <Header />

                <div className="flex flex-1 overflow-hidden relative">
                    <div className="flex-1 overflow-y-auto">
                        <MainContent />
                    </div>

                    <RightSidebar friendOpen={friendOpen} setFriendOpen={setFriendOpen} />

                    <FriendListPanel open={friendOpen} setOpen={setFriendOpen} />
                </div>

            </div>
        </div>
    );
}
