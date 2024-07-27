#ifndef DESKTOP
#define DESKTOP

#include <common.h>
#include <jGL/Display/desktopDisplay.h>

const int resX = 800;
const int resY = 1000;

uint8_t frameId = 0;
double deltas[60];

std::unique_ptr<jGL::jGLInstance> jGLInstance;

#endif /* DESKTOP */
