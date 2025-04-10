document.addEventListener("DOMContentLoaded", () => {
    const sendBtn = document.getElementById("sendBtn");
    const userInput = document.getElementById("userInput");
    const responseOutput = document.getElementById("responseOutput");

    sendBtn.addEventListener("click", async () => {
        const movieName = userInput.value.trim();
        if (!movieName) {
            responseOutput.innerHTML = "🐷 Skriv venligst en film!";
            return;
        }

        responseOutput.innerHTML = "🐷 Tænker...";

        try {
            const res = await fetch(`/api/movies/explore?title=${encodeURIComponent(movieName)}`);

            if (!res.ok) {
                if (res.status === 429) {
                    throw new Error("Vent venligst et øjeblik før du søger igen!");
                }
                throw new Error("Kunne ikke finde filmen");
            }

            const data = await res.json();

            // format
            const formattedResponse = `
                🎬 ${data.title} (${data.releaseDate}
                ${data.tagline ? `"${data.tagline}"` : ''}
                
                ⭐ Bedømmelse: ${data.rating}
                ⏱️ Længde: ${data.runtime}
                
                🐷 Kinogrisen siger:
                ${data.interestingTrivia}
                
                👥 Lignende film:
                ${data.similarMovies.map(movie =>
                `- ${movie.title} (${movie.releaseDate})`
            ).join('\n')}
            `;

            responseOutput.innerHTML = formattedResponse;
        } catch (err) {
            responseOutput.innerHTML = `🐷 ${err.message}`;
        }
    });
});