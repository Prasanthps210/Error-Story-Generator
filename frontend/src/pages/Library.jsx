import { useEffect, useState } from "react";
import { getHistory } from "../api/api";

export default function Library({ user }) {
    const [stories, setStories] = useState([]);

    useEffect(() => {
        getHistory(user.id).then(setStories);
    }, [user.id]);

    return (
        <div style={{ maxWidth: 800, margin: "40px auto" }}>
            <h2>Your Bug Bookshelf</h2>

            {stories.map((s, i) => (
                <div key={i} style={{ border: "1px solid #ccc", marginBottom: 10 }}>
                    <h4>{s.title}</h4>
                    <p>{s.story}</p>
                </div>
            ))}
        </div>
    );
}
