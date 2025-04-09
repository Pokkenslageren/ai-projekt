document.addEventListener("DOMContentLoaded", () => {
    const sendBtn = document.getElementById("sendBtn");
    const userInput = document.getElementById("userInput");
    const responseOutput = document.getElementById("responseOutput");

    sendBtn.addEventListener("click", async () => {
        const message = userInput.value.trim();
        if (!message) {
            responseOutput.innerHTML = "🐷 Please enter something!";
            return;
        }

        responseOutput.innerHTML = "🐷 Thinking...";

        try {
            const res = await fetch(`/chat1?message=${encodeURIComponent(message)}`);
            const data = await res.json();
            const reply = data[0]?.message?.content || "No response from pig 🐷";
            responseOutput.innerHTML = `🐷 ${reply}`;
        } catch (err) {
            responseOutput.innerHTML = `🐷 Error: ${err.message}`;
        }
    });
});
