import { useState } from "react";
import { generateStory } from "../api/api";
import StoryViewer from "../components/StoryViewer";

export default function Dashboard({ user }) {
    const [errorText, setErrorText] = useState("");
    const [difficulty, setDifficulty] = useState("FUNNY");
    const [story, setStory] = useState(null);

    const handleGenerate = async () => {
        try {
            const data = await generateStory(user.id, {
                errorText,
                difficulty
            });
            setStory(data);
        } catch {
            alert("Generation failed");
        }
    };

    return (
        <div style={{ maxWidth: 700, margin: "40px auto" }}>
            <h1>Error Story Generator</h1>

            <textarea
                rows={5}
                placeholder="Paste error here..."
                value={errorText}
                onChange={(e) => setErrorText(e.target.value)}
            />

            <select
                value={difficulty}
                onChange={(e) => setDifficulty(e.target.value)}
            >
                <option value="FUNNY">FUNNY</option>
                <option value="INTERMEDIATE">INTERMEDIATE</option>
            </select>

            <button onClick={handleGenerate}>Generate</button>

            {story && <StoryViewer story={story} />}
        </div>
    );
}
