export interface ToastMessage {
  id: number;
  kind: "success" | "error";
  text: string;
}

interface ToastStackProps {
  toasts: ToastMessage[];
  onDismiss: (id: number) => void;
}

export function ToastStack({ toasts, onDismiss }: ToastStackProps) {
  if (toasts.length === 0) return null;

  return (
    <div className="toast-stack">
      {toasts.map((toast) => (
        <div
          key={toast.id}
          className={`toast toast--${toast.kind}`}
          onClick={() => onDismiss(toast.id)}
        >
          {toast.text}
        </div>
      ))}
    </div>
  );
}
