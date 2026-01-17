import { marked } from "marked";

function StoryViewer({ story }) {
    if (!story) return null;

    const formattedStory = marked.parse(story.story);

    return (
        <div className="card">
            <h2 className="story-title">{story.title}</h2>

            <div
                className="story-text"
                dangerouslySetInnerHTML={{ __html: formattedStory }}
            />

            {/* FIX Section */}
            {story.explanation && (
                <>
                    <hr />
                    <h3>🛠 FIX</h3>
                    <p>{story.explanation.replace("FIX:", "").trim()}</p>
                </>
            )}

            {/* Example Code Section */}
            {story.example && story.example.trim() !== "" && (
                <>
                    <hr />
                    <h3>💻 Example Code</h3>
                    <pre style={{
                        background: "#020617",
                        padding: "12px",
                        borderRadius: "6px",
                        overflowX: "auto",
                        color: "#93c5fd",
                        fontSize: "14px"
                    }}>
                        {story.example}
                    </pre>
                </>
            )}
        </div>
    );
}

export default StoryViewer;
