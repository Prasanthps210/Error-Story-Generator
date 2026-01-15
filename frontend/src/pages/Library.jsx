import { useEffect, useState } from "react";
import { getHistory, deleteStory, updateStory } from "../api/api";

export default function Library({ user }) {
    const [stories, setStories] = useState([]);
    const [editingId, setEditingId] = useState(null);
    const [editData, setEditData] = useState({
        title: "",
        story: "",
        explanation: ""
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
            explanation: story.explanation || ""
        });
    };

    const handleUpdate = async () => {
        await updateStory(editingId, editData);
        setEditingId(null);
        loadStories();
    };

    return (
        <div style={{ maxWidth: 900, margin: "40px auto" }}>
            <h2>Your Bug Bookshelf</h2>

            {stories.map((s) => (
                <div
                    key={s.id}
                    style={{
                        border: "1px solid #334155",
                        padding: 15,
                        marginBottom: 15,
                        borderRadius: 6,
                    }}
                >
                    {editingId === s.id ? (
                        <>
                            <input
                                value={editData.title}
                                onChange={(e) =>
                                    setEditData({ ...editData, title: e.target.value })
                                }
                            />

                            <textarea
                                rows={4}
                                value={editData.story}
                                onChange={(e) =>
                                    setEditData({ ...editData, story: e.target.value })
                                }
                            />

                            <textarea
                                rows={2}
                                placeholder="Fix / Explanation"
                                value={editData.explanation}
                                onChange={(e) =>
                                    setEditData({ ...editData, explanation: e.target.value })
                                }
                            />

                            <button onClick={handleUpdate}>Save</button>
                            <button onClick={() => setEditingId(null)}>Cancel</button>
                        </>
                    ) : (
                        <>
                            <h4>{s.title}</h4>
                            <p>{s.story}</p>
                            {s.explanation && <p><b>Fix:</b> {s.explanation}</p>}

                            <button onClick={() => handleEdit(s)}>Edit</button>
                            <button onClick={() => handleDelete(s.id)}>Delete</button>
                        </>
                    )}
                </div>
            ))}
        </div>
    );
}
