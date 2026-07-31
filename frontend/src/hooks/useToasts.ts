import { useCallback, useState } from "react";
import type { ToastMessage } from "../components/Toast";

let nextId = 1;

export function useToasts() {
  const [toasts, setToasts] = useState<ToastMessage[]>([]);

  const push = useCallback((kind: ToastMessage["kind"], text: string) => {
    const id = nextId++;
    setToasts((prev) => [...prev, { id, kind, text }]);
    setTimeout(() => {
      setToasts((prev) => prev.filter((t) => t.id !== id));
    }, 5000);
  }, []);

  const dismiss = useCallback((id: number) => {
    setToasts((prev) => prev.filter((t) => t.id !== id));
  }, []);

  return {
    toasts,
    success: (text: string) => push("success", text),
    error: (text: string) => push("error", text),
    dismiss,
  };
}
