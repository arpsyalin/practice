package com.lyl.pluginmanage.reflectionlimit;

import com.lyl.pluginmanage.VersionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * * @Description 9.0反射限制问题
 * * @Author 刘亚林
 * * @CreateDate 2020/11/29
 * * @Version 1.0
 * * @Remark TODO
 **/
public final class ReflectionLimit {
    //todo 反射获取不到 getService 原因：Android 9.0 私有API禁用机制
    // //-----------------------------------
    // https://www.androidos.net.cn/android/10.0.0_r6/xref/art/runtime/native/java_lang_Class.cc
    // private native Method getDeclaredMethodInternal(String name, Class<?>[] args);
    // static jobject Class_getDeclaredMethodInternal(JNIEnv* env, jobject javaThis,
    //                                               jstring name, jobjectArray args) {
    //  ScopedFastNativeObjectAccess soa(env);
    //  StackHandleScope<1> hs(soa.Self());
    //  DCHECK_EQ(Runtime::Current()->GetClassLinker()->GetImagePointerSize(), kRuntimePointerSize);
    //  DCHECK(!Runtime::Current()->IsActiveTransaction());
    //  Handle<mirror::Method> result = hs.NewHandle(
    //      mirror::Class::GetDeclaredMethodInternal<kRuntimePointerSize, false>(
    //          soa.Self(),
    //          DecodeClass(soa, javaThis),
    //          soa.Decode<mirror::String>(name),
    //          soa.Decode<mirror::ObjectArray<mirror::Class>>(args),
    //          GetHiddenapiAccessContextFunction(soa.Self())));
    //  if (result == nullptr || ShouldDenyAccessToMember(result->GetArtMethod(), soa.Self())) {
    //    return nullptr;
    //  }
    //  return soa.AddLocalReference<jobject>(result.Get());
    // }
    // ALWAYS_INLINE static bool ShouldDenyAccessToMember(T* member, Thread* self)
    //    REQUIRES_SHARED(Locks::mutator_lock_) {
    //  return hiddenapi::ShouldDenyAccessToMember(member,
    //                                             GetHiddenapiAccessContextFunction(self),
    //                                             hiddenapi::AccessMethod::kReflection);
    // }
    // //-----------------------------------
    // https://www.androidos.net.cn/android/10.0.0_r6/xref/art/runtime/hidden_api.h
    // template<typename T>
    // inline bool ShouldDenyAccessToMember(T* member,
    //                                     const std::function<AccessContext()>& fn_get_access_context,
    //                                     AccessMethod access_method)
    //    REQUIRES_SHARED(Locks::mutator_lock_) {
    //  DCHECK(member != nullptr);
    //   // Get the runtime flags encoded in member's access flags.
    //  // Note: this works for proxy methods because they inherit access flags from their
    //  // respective interface methods.
    //  const uint32_t runtime_flags = GetRuntimeFlags(member);
    //  // Exit early if member is public API. This flag is also set for non-boot class
    //  // path fields/methods.
    //  if ((runtime_flags & kAccPublicApi) != 0) {
    //    return false;
    //  }
    //  // Determine which domain the caller and callee belong to.
    //  // This can be *very* expensive. This is why ShouldDenyAccessToMember
    //  // should not be called on every individual access.
    //  const AccessContext caller_context = fn_get_access_context();
    //  const AccessContext callee_context(member->GetDeclaringClass());
    //  // Non-boot classpath callers should have exited early.
    //  DCHECK(!callee_context.IsApplicationDomain());
    //  // Check if the caller is always allowed to access members in the callee context.
    //  if (caller_context.CanAlwaysAccess(callee_context)) {
    //    return false;
    //  }
    //  // Check if this is platform accessing core platform. We may warn if `member` is
    //  // not part of core platform API.
    //  switch (caller_context.GetDomain()) {
    //    case Domain::kApplication: {
    //      DCHECK(!callee_context.IsApplicationDomain());
    //      // Exit early if access checks are completely disabled.
    //      //这句从Runtime中获取API策略
    //      EnforcementPolicy policy = Runtime::Current()->GetHiddenApiEnforcementPolicy();
    //      if (policy == EnforcementPolicy::kDisabled) {
    //        return false;
    //      }
    //      // If this is a proxy method, look at the interface method instead.
    //      member = detail::GetInterfaceMemberIfProxy(member);
    //      // Decode hidden API access flags from the dex file.
    //      // This is an O(N) operation scaling with the number of fields/methods
    //      // in the class. Only do this on slow path and only do it once.
    //      ApiList api_list(detail::GetDexFlags(member));
    //      DCHECK(api_list.IsValid());
    //      // Member is hidden and caller is not exempted. Enter slow path.
    //      return detail::ShouldDenyAccessToMemberImpl(member, api_list, access_method);
    //    }
    //    case Domain::kPlatform: {
    //      DCHECK(callee_context.GetDomain() == Domain::kCorePlatform);
    //      // Member is part of core platform API. Accessing it is allowed.
    //      if ((runtime_flags & kAccCorePlatformApi) != 0) {
    //        return false;
    //      }
    //      // Allow access if access checks are disabled.
    //      EnforcementPolicy policy = Runtime::Current()->GetCorePlatformApiEnforcementPolicy();
    //      if (policy == EnforcementPolicy::kDisabled) {
    //        return false;
    //      }
    //      // If this is a proxy method, look at the interface method instead.
    //      member = detail::GetInterfaceMemberIfProxy(member);
    //      // Access checks are not disabled, report the violation.
    //      // This may also add kAccCorePlatformApi to the access flags of `member`
    //      // so as to not warn again on next access.
    //      return detail::HandleCorePlatformApiViolation(member,
    //                                                    caller_context,
    //                                                    access_method,
    //                                                    policy);
    //    }
    //    case Domain::kCorePlatform: {
    //      LOG(FATAL) << "CorePlatform domain should be allowed to access all domains";
    //      UNREACHABLE();
    //    }
    //  }
    // }
    // //-----------------------------------
    // https://www.androidos.net.cn/android/10.0.0_r6/xref/art/runtime/runtime.h
    //  hiddenapi::EnforcementPolicy GetCorePlatformApiEnforcementPolicy() const {
    //    return core_platform_api_policy_;
    //  }
    //   //可以设置core_platform_api_policy_
    //   void SetCorePlatformApiEnforcementPolicy(hiddenapi::EnforcementPolicy policy) {
    //    core_platform_api_policy_ = policy;
    //  }
    //  //定义
    //    // Whether access checks on core platform API should be performed.
    //  hiddenapi::EnforcementPolicy core_platform_api_policy_;
    // //-----------------------------------
    //   https://www.androidos.net.cn/android/10.0.0_r6/xref/art/runtime/hidden_api.h
    //  //枚举类
    // enum class EnforcementPolicy {
    //  kDisabled             = 0,
    //  kJustWarn             = 1,  // keep checks enabled, but allow everything (enables logging)
    //  kEnabled              = 2,  // ban dark grey & blacklist
    //  kMax = kEnabled,
    // };
    // 上面得出结论，可以通过JNI找到这个类的实例，通过仿写一个结构体，然后指针替换赋值一下。于是找这个相关联的实例。
    // //-----------------------------------
    // https://www.androidos.net.cn/android/10.0.0_r6/xref/libcore/libart/src/main/java/dalvik/system/VMRuntime.java
    //  private static final VMRuntime THE_ONE = new VMRuntime();
    //  @UnsupportedAppUsage
    //    @libcore.api.CorePlatformApi
    //    public static VMRuntime getRuntime() {
    //        return THE_ONE;
    //    }
    // dalvik.system.VMRuntime
    //  /**
    //     * Sets the list of exemptions from hidden API access enforcement.
    //     *
    //     * @param signaturePrefixes
    //     *         A list of signature prefixes. Each item in the list is a prefix match on the type
    //     *         signature of a blacklisted API. All matching APIs are treated as if they were on
    //     *         the whitelist: access permitted, and no logging..
    //     */
    //    @libcore.api.CorePlatformApi
    //    public native void setHiddenApiExemptions(String[] signaturePrefixes);
    // //-----------------------------------
    // https://www.androidos.net.cn/android/10.0.0_r6/xref/art/runtime/native/dalvik_system_VMRuntime.cc
    // static void VMRuntime_setHiddenApiExemptions(JNIEnv* env,
    //                                            jclass,
    //                                            jobjectArray exemptions) {
    //  std::vector<std::string> exemptions_vec;
    //  int exemptions_length = env->GetArrayLength(exemptions);
    //  for (int i = 0; i < exemptions_length; i++) {
    //    jstring exemption = reinterpret_cast<jstring>(env->GetObjectArrayElement(exemptions, i));
    //    const char* raw_exemption = env->GetStringUTFChars(exemption, nullptr);
    //    exemptions_vec.push_back(raw_exemption);
    //    env->ReleaseStringUTFChars(exemption, raw_exemption);
    //  }
    //   Runtime::Current()->SetHiddenApiExemptions(exemptions_vec);
    // }
    // 得出结论最简单可以直接反射 invoke setHiddenApiExemptions
    private static Object sVMRuntime;
    private static Method setHiddenApiExemptions;

    static {
        if (VersionUtils.isAndroid10_11()) {
            try {
                Method forName = Class.class.getDeclaredMethod("forName", String.class);
                Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
                Class<?> vmRuntimeClass = (Class<?>) forName.invoke(null, "dalvik.system.VMRuntime");
                Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
                setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[]{String[].class});
                setHiddenApiExemptions.setAccessible(true);
                sVMRuntime = getRuntime.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //消除限制
    public static boolean clearLimit() {
        if (sVMRuntime == null || setHiddenApiExemptions == null) {
            return false;
        }
        try {
            setHiddenApiExemptions.invoke(sVMRuntime, new Object[]{new String[]{"L"}});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
