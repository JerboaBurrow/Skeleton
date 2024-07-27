#include <jni.h>

#include <EGL/egl.h>
#include <EGL/eglext.h>
#include <GLES3/gl3.h>

#include <android/log.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>

#include <math.h>
#include <algorithm>
#include <memory>

#include <jGL/orthoCam.h>
#include <jGL/jGL.h>
#include <jGL/OpenGL/openGLInstance.h>

#include <jLog/jLog.h>

#include <headers/json.hpp>
using json = nlohmann::json;

using jGL::OrthoCam;
using jLog::INFO;
using jLog::WARN;

static std::shared_ptr<jLog::Log> hopLog = nullptr;
static std::shared_ptr<OrthoCam> camera = nullptr;
static std::shared_ptr<jGL::jGLInstance> jgl = nullptr;

std::string fixedLengthNumber(double x, unsigned length);

std::string jstring2string(JNIEnv *env, jstring jStr) {

    if (!jStr) {return "";}

    const jclass stringClass = env->GetObjectClass(jStr);
    const jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
    const jbyteArray stringJbytes = (jbyteArray) env->CallObjectMethod(jStr, getBytes, env->NewStringUTF("UTF-8"));

    size_t length = (size_t) env->GetArrayLength(stringJbytes);
    jbyte* pBytes = env->GetByteArrayElements(stringJbytes, NULL);

    std::string ret = std::string((char *)pBytes, length);
    env->ReleaseByteArrayElements(stringJbytes, pBytes, JNI_ABORT);

    env->DeleteLocalRef(stringJbytes);
    env->DeleteLocalRef(stringClass);
    return ret;
}

extern "C"
{
void Java_app_jerboa_skeleton_Hop_initialise(
        JNIEnv *env,
        jobject /* this */,
        jint resX,
        jint resY,
        jboolean skipTutorial
)
{

    // android may store state between certain life-cycle events
    //  must be absolutely sure to delete the old memory otherwise
    //  we sometimes end up with a blank screen (but the game still runs!)
    hopLog = nullptr;
    camera = nullptr;
    jgl = nullptr;

    hopLog = std::make_shared<jLog::Log>();

    camera = std::make_shared<jGL::OrthoCam>(resX, resY, glm::vec2(0.0,0.0));

    jgl = std::make_shared<jGL::GL::OpenGLInstance>(glm::ivec2(resX, resY), 0);

    jgl->setTextProjection(glm::ortho(0.0,double(resX),0.0,double(resY)));
    jgl->setMSAA(0);

    hopLog->androidLog();
}

void Java_app_jerboa_skeleton_Hop_tap(JNIEnv *env,
                                      jobject,
                                      float sx,
                                      float sy)
{
    glm::vec4 w = camera->screenToWorld(sx,sy);
    float x = w.x;
    float y = 1.0-w.y;
}

void Java_app_jerboa_skeleton_Hop_swipe(JNIEnv *env,
                                        jobject,
                                        float vx,
                                        float vy)
{}

void Java_app_jerboa_skeleton_Hop_loop
        (
                JNIEnv *env,
                jobject,
                jint frameId,
                jboolean first
        )
{
    jgl->beginFrame();

    jgl->clear();

    jgl->endFrame();
}

void Java_app_jerboa_skeleton_Hop_printLog
        (
                JNIEnv *env,
                jobject /* this */
        )
{
    if (hopLog != nullptr)
    {
        hopLog->androidLog();
    }
}

void Java_app_jerboa_skeleton_Hop_beginFrame(JNIEnv * env, jobject /* this */)
{
    jgl->beginFrame();
}

void Java_app_jerboa_skeleton_Hop_endFrame(JNIEnv * env, jobject /* this */)
{
    jgl->endFrame();
}

void Java_app_jerboa_skeleton_Hop_render(
        JNIEnv *env,
        jobject /* this */,
        jboolean refresh
)
{
}

void Java_app_jerboa_skeleton_Hop_renderText
        (
                JNIEnv *env,
                jobject /* this */,
                jstring text,
                jfloat x,
                jfloat y,
                jfloat scale,
                jfloat r,
                jfloat g,
                jfloat b,
                jfloat a,
                jboolean centredx,
                jboolean centredy
        )
{
    if (jgl == nullptr){
        return;
    }

    jgl->text
            (
                    jstring2string(env,text).c_str(),
                    glm::vec2(x, y),
                    scale,
                    glm::vec4(r,g,b,a),
                    glm::bvec2(centredx, centredy)
            );
}

void Java_app_jerboa_skeleton_Hop_getWorldPosition
        (
                JNIEnv *env,
                jobject /* this */
        );

void Java_app_jerboa_skeleton_Hop_screenToWorld
        (
                JNIEnv *env,
                jobject /* this */,
                jfloat x,
                jfloat y,
                jfloat rx,
                jfloat ry
        ) {
    if (camera == nullptr) {
        return;
    }

    glm::vec4 w = camera->screenToWorld(x,y);
    rx = w[0];
    ry = w[1];
}
}