document.addEventListener("DOMContentLoaded", () => {
    const sendBtn = document.getElementById("sendBtn");
    const userInput = document.getElementById("userInput");
    const responseOutput = document.getElementById("responseOutput");

    sendBtn.addEventListener("click", async () => {
        const movieName = userInput.value.trim();
        if (!movieName) {
            responseOutput.innerHTML = "🐷 Please enter a movie!";
            return;
        }

        responseOutput.innerHTML = "🐷 Tænker...";

        try {
            const res = await fetch(`/test?movie=${encodeURIComponent(movieName)}`);

            const data = await res.json();

            const reply = data.Choices?.[0]?.message?.content || "🐷 Intet svar!";
            responseOutput.innerHTML = `🐷 ${reply}`;
        } catch (err) {
            responseOutput.innerHTML = `🐷 Error: ${err.message}`;
        }
    });
});