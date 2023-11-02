/// <reference types="vite/client" />
interface ImportMetaEnv {
readonly VITE_GOOGLE_ANALYTICS_ID: string; //추가
}

interface ImportMeta {
readonly env: ImportMetaEnv;
}