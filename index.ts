// npm install typescript @types/node raylib
// npm rebuild raylib

// npx tsc --noEmit

// UnloadTexture(texture);
// UnloadModel(model);

// terrain.png https://www.planetminecraft.com/texture-pack/default-texture-pack-beta-1-7-3/

const greeting: string = "Hello from Native TypeScript in Node!";
console.log(greeting);

const r = require("raylib");

r.InitWindow(500, 500, "Game");
r.SetTargetFPS(60);

const camera: Camera3D = {
    position: { x: 0.0, y: 3.0, z: 3.0 },
    target: { x: 0.0, y: 0.0, z: 0.0 },
    up: { x: 0.0, y: 1.0, z: 0.0 },
    fovy: 45.0,
    projection: r.CAMERA_PERSPECTIVE
};

const model: Model = r.LoadModelFromMesh(r.GenMeshCube(1.0, 1.0, 1.0));

let rotation: float = 0.0;

while (!r.WindowShouldClose()) {

    rotation += 1.0;

    r.BeginDrawing();

    r.ClearBackground(r.RAYWHITE);
    r.DrawText("game", 0, 0, 20, r.LIGHTGRAY);
    r.DrawFPS(0, 20);

    r.BeginMode3D(camera);

    r.DrawModelEx(model, { x: 0.0, y: 0.0, z: 0.0 }, { x: 0.5, y: 1.0, z: 0.0 }, rotation, { x: 1.0, y: 1.0, z: 1.0 }, r.WHITE);

    r.DrawGrid(10, 1.0);

    r.EndMode3D();
    r.EndDrawing();
}