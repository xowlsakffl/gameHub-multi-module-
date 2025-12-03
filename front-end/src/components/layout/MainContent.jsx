import { useEffect, useRef, useState } from "react";
import axios from "axios";
import SimpleBar from 'simplebar-react';
import PartyCard from "../party/PartyCard.jsx";

export default function MainContent() {
    const [parties, setParties] = useState([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const [loading, setLoading] = useState(false);

    const observerRef = useRef(null);

    // API 로딩
    const fetchParties = async (pageNum) => {
        setLoading(true);

        try {
            const res = await axios.get("/api/party", {
                params: {
                    page: pageNum,
                    size: 10,
                    sortBy: "createdAt",
                    direction: "DESC",
                },
            });

            const pageData = res.data?.data;
            setParties((prev) => [...prev, ...pageData.content]);
            setTotalPages(pageData.totalPages);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchParties(0);
    }, []);

    useEffect(() => {
        if (!observerRef.current) return;

        const observer = new IntersectionObserver(
            (entries) => {
                const entry = entries[0];

                if (entry.isIntersecting && !loading && page + 1 < totalPages) {
                    const next = page + 1;
                    setPage(next);
                    fetchParties(next);
                }
            },
            { threshold: 1 }
        );

        observer.observe(observerRef.current);
        return () => observer.disconnect();
    }, [page, totalPages, loading]);

    return (
        <SimpleBar style={{ height: "calc(100vh - 80px)" }}>
            <div className="p-6">
                <h2 className="text-2xl font-semibold mb-4 text-white">
                    현재 오픈 파티
                </h2>

                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 max-w-4xl mx-auto">
                    {parties.map((party) => (
                        <PartyCard key={party.id} party={party} />
                    ))}
                </div>

                {/* 무한 스크롤 트리거 */}
                <div ref={observerRef} className="h-16" />

                {loading && (
                    <div className="text-center text-gray-400 p-4">
                        불러오는 중...
                    </div>
                )}
            </div>
        </SimpleBar>
    );
}
