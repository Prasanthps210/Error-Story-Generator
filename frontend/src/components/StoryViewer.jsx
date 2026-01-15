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

            {story.explanation && (
                <>
                    <hr />
                    <h3>Explanation</h3>
                    <p>{story.explanation}</p>
                </>
            )}
        </div>
    );
}

export default StoryViewer;
