interface StatusBadgeProps {
  value: string;
  tone?: "neutral" | "info" | "warning" | "success" | "danger";
}

const TONE_BY_VALUE: Record<string, StatusBadgeProps["tone"]> = {
  DRAFT: "neutral",
  SCHEDULED: "info",
  ONGOING: "warning",
  COMPLETED: "info",
  PUBLISHED: "success",
  CANCELLED: "danger",
  PASS: "success",
  FAIL: "danger",
  A_PLUS: "success",
  A: "success",
  B_PLUS: "info",
  B: "info",
  C: "warning",
  D: "warning",
  F: "danger",
  ACTIVE: "success",
  INACTIVE: "neutral",
  SUSPENDED: "danger",
  ALUMNI: "info",
  PRESENT: "success",
  ABSENT: "danger",
  LATE: "warning",
  HALF_DAY: "warning",
  LEAVE: "neutral",
  CORE: "info",
  ELECTIVE: "neutral",
  OPTIONAL: "neutral",
  PRACTICAL: "warning",
};

export function StatusBadge({ value, tone }: StatusBadgeProps) {
  const resolvedTone = tone ?? TONE_BY_VALUE[value] ?? "neutral";

  return (
    <span className={`badge badge--${resolvedTone}`}>
      {value.replace("_", "+")}
    </span>
  );
}
