import { createRouter, createWebHistory } from "vue-router";
import { useAuthStore } from "@/stores/auth.js";
import authRoutes from "./auth.js";

// 페이지 컴포넌트 import
import Home from "@/pages/Home.vue";
import Profile from "@/pages/Profile.vue";
import AdminUsers from "@/pages/AdminUsers.vue";

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
    meta: {
      requiresAuth: false,
      title: "홈",
    },
  },
  {
    path: "/profile",
    name: "Profile",
    component: Profile,
    meta: {
      requiresAuth: true,
      title: "프로필",
      roles: ["USER", "MANAGER", "ADMIN"],
    },
  },
  {
    path: "/adminUsers",
    name: "AdminUsers",
    component: AdminUsers,
    meta: {
      requiresAuth: true,
      title: "사용자 관리",
      roles: ["ADMIN"],
    },
  },

  // 인증 라우트 추가
  ...authRoutes,

  // 404 vpdlwl
  {
    path: "/:pathMatch(.*)*",
    name: "NotFound",
    redirect: "/",
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});


router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = `${to.meta.title} - My App`;
  }


  if (to.meta.requiresAuth) {
    const accessToken = localStorage.getItem("accessToken");
    if (!accessToken) {
      next({
        name: "Login",
        query: { redirect: to.fullPath },
      });
      return;
    }


    if (to.meta.roles && to.meta.roles.length > 0) {
      const authStore = useAuthStore();
      const userRole = authStore.userRole;
      console.log("권한 체크:", {
        requiredRoles: to.meta.roles,
        userRole: userRole,
        hasPermission: to.meta.roles.includes(userRole)
      });

      if (!to.meta.roles.includes(userRole)) {

        next({ name: "Home" });
        return;
      }
    }
  }

  if (to.name === "Login" || to.name === "Register") {
    const accessToken = localStorage.getItem("accessToken");

    if (accessToken) {
      const redirect = to.query.redirect || "/";
      next(redirect);
      return;
    }
  }
  next();
});

export default router;
