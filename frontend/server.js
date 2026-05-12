const http = require("http");
const fs = require("fs");
const path = require("path");

const PORT = 5500;
const HOST = "127.0.0.1";
const ROOT = __dirname;

const MIME_TYPES = {
    ".html": "text/html; charset=utf-8",
    ".css": "text/css; charset=utf-8",
    ".js": "application/javascript; charset=utf-8",
    ".json": "application/json; charset=utf-8",
    ".png": "image/png",
    ".jpg": "image/jpeg",
    ".jpeg": "image/jpeg",
    ".svg": "image/svg+xml",
    ".ico": "image/x-icon"
};

const server = http.createServer((request, response) => {
    const requestPath = request.url === "/" ? "/index.html" : request.url;
    const safePath = path.normalize(requestPath).replace(/^(\.\.[/\\])+/, "");
    const filePath = path.join(ROOT, safePath);

    if (!filePath.startsWith(ROOT)) {
        response.writeHead(403);
        response.end("Forbidden");
        return;
    }

    fs.readFile(filePath, (error, content) => {
        if (error) {
            response.writeHead(error.code === "ENOENT" ? 404 : 500, {
                "Content-Type": "text/plain; charset=utf-8"
            });
            response.end(error.code === "ENOENT" ? "Not Found" : "Server Error");
            return;
        }

        const ext = path.extname(filePath).toLowerCase();
        response.writeHead(200, {
            "Content-Type": MIME_TYPES[ext] || "application/octet-stream"
        });
        response.end(content);
    });
});

server.listen(PORT, HOST, () => {
    console.log(`Frontend server is running at http://${HOST}:${PORT}`);
});
