import { useState, useEffect } from "react";
import { generateStory } from "../api/api";
import StoryViewer from "../components/StoryViewer";

export default function Dashboard({ user }) {
    const [errorText, setErrorText] = useState("");
    const [difficulty, setDifficulty] = useState("BEGINNER");
    const [story, setStory] = useState(null);
    const [loading, setLoading] = useState(false);

    const [mode, setMode] = useState("AI");          // AI or DEFAULT
    const [isOnline, setIsOnline] = useState(navigator.onLine);

    // 🔌 Listen for internet status change
    useEffect(() => {
        const handleOnline = () => setIsOnline(true);
        const handleOffline = () => setIsOnline(false);

        window.addEventListener("online", handleOnline);
        window.addEventListener("offline", handleOffline);

        return () => {
            window.removeEventListener("online", handleOnline);
            window.removeEventListener("offline", handleOffline);
        };
    }, []);

    // 🔒 Force DEFAULT when offline
    useEffect(() => {
        if (!isOnline) {
            setMode("DEFAULT");
        }
    }, [isOnline]);

    const handleGenerate = async () => {

        if (!errorText.trim()) {
            alert("Please paste an error message before generating a story.");
            return;
        }
        try {
            setLoading(true);
            setStory(null);

            const data = await generateStory(user.id, {
                errorText,
                difficulty,
                mode
            });

            setStory(data);
        } catch (err) {
            alert("Generation failed");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="generator-box" style={{ maxWidth: 700, margin: "40px auto" }}>
            <h1>Error Story Generator</h1>

            <textarea
                rows={5}
                placeholder="Paste error here..."
                value={errorText}
                onChange={(e) => setErrorText(e.target.value)}
            />
            <div className="difficulty-box">
            <select
                value={difficulty}
                onChange={(e) => setDifficulty(e.target.value)}
            >
                <option value="BEGINNER">BEGINNER</option>
                <option value="INTERMEDIATE">INTERMEDIATE</option>
                <option value="ADVANCED">ADVANCED</option>
            </select>
            </div>

            {/* Mode Buttons */}
            <div style={{ display: "flex", gap: "12px" }}>
                {/* AI Button */}
                <button
                    style={{
                        flex: 1,
                        padding: "14px",
                        fontSize: "16px",
                        background: isOnline && mode === "AI" ? "#22c55e" : "#334155",
                        opacity: isOnline ? 1 : 0.4,
                        cursor: isOnline ? "pointer" : "not-allowed"
                    }}
                    onClick={() => isOnline && setMode("AI")}
                    disabled={!isOnline || loading}
                >
                    🤖 Use AI {isOnline ? "" : "(Offline)"}
                </button>

                {/* Default Button */}
                <button
                    style={{
                        flex: 1,
                        padding: "14px",
                        fontSize: "16px",
                        background: mode === "DEFAULT" ? "#22c55e" : "#334155",
                        opacity: 1
                    }}
                    onClick={() => setMode("DEFAULT")}
                    disabled={loading}
                >
                    🧠 Default
                </button>
            </div>

            {/* Generate Button */}
            <button
                style={{
                    width: "100%",
                    padding: "16px",
                    fontSize: "18px",
                    marginTop: "10px",
                    opacity: (!errorText.trim() || loading) ? 0.5 : 1,
                    cursor: (!errorText.trim() || loading) ? "not-allowed" : "pointer"
                }}
                onClick={handleGenerate}
                disabled={loading}
            >
                {loading ? "Generating..." : "Generate Story"}
            </button>

            {loading && (
                <p className="loading-text" style={{ color: "#93c5fd" }}>
                    please wait...
                </p>
            )}

            {story && <StoryViewer story={story} />}
        </div>
    );
}
