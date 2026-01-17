import { useEffect, useState, useRef } from "react";
import { getHistory, deleteStory, updateStory } from "../api/api";

export default function Library({ user }) {
    const [stories, setStories] = useState([]);
    const [editingId, setEditingId] = useState(null);
    const [editData, setEditData] = useState({
        title: "",
        story: "",
        explanation: "",
        example: ""
    });

    useEffect(() => {
        loadStories();
    }, [user.id]);

    const loadStories = () => {
        getHistory(user.id).then(setStories);
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Delete this story?")) return;
        await deleteStory(id);
        loadStories();
    };

    const handleEdit = (story) => {
        setEditingId(story.id);
        setEditData({
            title: story.title,
            story: story.story,
            explanation: story.explanation || "",
            example: story.example || ""
        });
        setTimeout(() => {
            resizeTextarea(storyRef);
            resizeTextarea(fixRef);
            resizeTextarea(exampleRef);
        }, 0);
    };

    const handleUpdate = async () => {
        await updateStory(editingId, editData);
        setEditingId(null);
        loadStories();
    };

    const storyRef = useRef(null);
    const fixRef = useRef(null);
    const exampleRef = useRef(null);

    const resizeTextarea = (ref) => {
        if (ref.current) {
            ref.current.style.height = "auto";
            ref.current.style.height = ref.current.scrollHeight + "px";
        }
    };



    return (
        <div style={{ maxWidth: 900, margin: "40px auto" }}>
            <h2>Your Bug Bookshelf</h2>

            {stories.map((s) => (
                <div key={s.id} className="story-card">
                    {editingId === s.id ? (
                        <div className="edit-box">
                            <input
                                value={editData.title}
                                onChange={(e) =>
                                    setEditData({ ...editData, title: e.target.value })
                                }
                            />

                            <textarea
                                ref={storyRef}
                                value={editData.story}
                                onChange={(e) => {
                                    setEditData({ ...editData, story: e.target.value });
                                    resizeTextarea(storyRef);
                                }}
                            />

                            <textarea
                                ref={fixRef}
                                value={editData.explanation}
                                onChange={(e) => {
                                    setEditData({ ...editData, explanation: e.target.value });
                                    resizeTextarea(fixRef);
                                }}
                            />

                            <textarea
                                ref={exampleRef}
                                value={editData.example}
                                onChange={(e) => {
                                    setEditData({ ...editData, example: e.target.value });
                                    resizeTextarea(exampleRef);
                                }}
                            />


                            <div className="edit-actions">
                                <button onClick={handleUpdate}>Save</button>
                                <button onClick={() => setEditingId(null)}>Cancel</button>
                            </div>
                        </div>
                    ) : (
                        <>
                            <h3 className="story-title">{s.title}</h3>

                            <p className="story-text">{s.story}</p>

                            {s.explanation && (
                                <div className="fix-box">
                                    <b>🛠 Fix</b>
                                    <p>{s.explanation}</p>
                                </div>
                            )}

                            {s.example && s.example.trim() !== "" && (
                                <>
                                    <b>💻 Example Code</b>
                                    <pre className="code-box">{s.example}</pre>
                                </>
                            )}

                            <div className="action-row">
                                <button onClick={() => handleEdit(s)}>Edit</button>
                                <button onClick={() => handleDelete(s.id)}>Delete</button>
                            </div>
                        </>
                    )}
                </div>
            ))}
        </div>
    );
}
