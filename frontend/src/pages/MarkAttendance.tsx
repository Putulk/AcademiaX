import { useEffect, useMemo, useState } from "react";
import { attendanceApi } from "../api/attendanceApi";
import { ApiError } from "../api/client";
import {
  loadEnrichedClassSections,
  loadEnrichedStudentEnrollments,
  loadSubjectOptions,
  loadTeacherOptions,
  type EnrichedClassSection,
  type EnrichedEnrollment,
  type ReferenceOption,
} from "../api/directory";
import { ToastStack } from "../components/Toast";
import { useToasts } from "../hooks/useToasts";
import { ATTENDANCE_STATUSES, type AttendanceStatus } from "../types/attendance";

interface RowState {
  attendanceId?: string;
  status: AttendanceStatus;
  remarks: string;
}

export function MarkAttendance() {
  const [classSections, setClassSections] = useState<EnrichedClassSection[]>([]);
  const [subjects, setSubjects] = useState<ReferenceOption[]>([]);
  const [teachers, setTeachers] = useState<ReferenceOption[]>([]);
  const [allEnrollments, setAllEnrollments] = useState<EnrichedEnrollment[]>([]);
  const [loadingSetup, setLoadingSetup] = useState(true);

  const [classSectionId, setClassSectionId] = useState("");
  const [subjectId, setSubjectId] = useState("");
  const [teacherId, setTeacherId] = useState("");
  const [date, setDate] = useState(() => new Date().toISOString().slice(0, 10));

  const [rows, setRows] = useState<Record<string, RowState>>({});
  const [loadingRoster, setLoadingRoster] = useState(false);
  const [saving, setSaving] = useState(false);
  const toasts = useToasts();

  useEffect(() => {
    Promise.all([
      loadEnrichedClassSections(),
      loadSubjectOptions(),
      loadTeacherOptions(),
      loadEnrichedStudentEnrollments(),
    ])
      .then(([cs, subj, tch, enrollments]) => {
        setClassSections(cs);
        setSubjects(subj);
        setTeachers(tch);
        setAllEnrollments(enrollments);
      })
      .catch((err) =>
        toasts.error(err instanceof ApiError ? err.message : "Failed to load setup data"),
      )
      .finally(() => setLoadingSetup(false));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const roster = useMemo(
    () => allEnrollments.filter((e) => e.classSectionId === classSectionId),
    [allEnrollments, classSectionId],
  );

  useEffect(() => {
    if (!classSectionId || !subjectId || !date || roster.length === 0) {
      setRows({});
      return;
    }

    setLoadingRoster(true);
    attendanceApi
      .listByDate(date)
      .then((existing) => {
        const rosterIds = new Set(roster.map((r) => r.id));
        const existingByEnrollment = new Map(
          existing
            .filter((a) => a.subjectId === subjectId && rosterIds.has(a.studentEnrollmentId))
            .map((a) => [a.studentEnrollmentId, a]),
        );

        const next: Record<string, RowState> = {};
        roster.forEach((r) => {
          const match = existingByEnrollment.get(r.id);
          next[r.id] = match
            ? { attendanceId: match.id, status: match.status, remarks: match.remarks ?? "" }
            : { status: "PRESENT", remarks: "" };
        });
        setRows(next);
      })
      .catch((err) =>
        toasts.error(
          err instanceof ApiError ? err.message : "Failed to load existing attendance",
        ),
      )
      .finally(() => setLoadingRoster(false));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [classSectionId, subjectId, date, roster.length]);

  const setRowStatus = (enrollmentId: string, status: AttendanceStatus) => {
    setRows((prev) => ({ ...prev, [enrollmentId]: { ...prev[enrollmentId], status } }));
  };

  const setRowRemarks = (enrollmentId: string, remarks: string) => {
    setRows((prev) => ({ ...prev, [enrollmentId]: { ...prev[enrollmentId], remarks } }));
  };

  const markAllPresent = () => {
    setRows((prev) => {
      const next = { ...prev };
      roster.forEach((r) => {
        next[r.id] = { ...next[r.id], status: "PRESENT" };
      });
      return next;
    });
  };

  const canSave = classSectionId && subjectId && teacherId && date && roster.length > 0;

  const save = async () => {
    const classSection = classSections.find((cs) => cs.id === classSectionId);
    if (!classSection) return;

    setSaving(true);
    const results = await Promise.allSettled(
      roster.map((r) => {
        const row = rows[r.id];
        const payload = {
          studentEnrollmentId: r.id,
          classId: classSection.classRoomId,
          sectionId: classSection.sectionId,
          subjectId,
          teacherId,
          attendanceDate: date,
          status: row.status,
          remarks: row.remarks,
        };
        return row.attendanceId
          ? attendanceApi.update(row.attendanceId, payload)
          : attendanceApi.create(payload);
      }),
    );

    const failed = results.filter((r) => r.status === "rejected").length;
    const succeeded = results.length - failed;

    if (failed === 0) {
      toasts.success(`Saved attendance for ${succeeded} student${succeeded === 1 ? "" : "s"}`);
    } else {
      toasts.error(`Saved ${succeeded}, failed ${failed} — check the failed rows and retry`);
    }

    setSaving(false);
    // Reload so newly-created rows pick up their attendanceId (future saves update, not duplicate).
    setDate((d) => d);
  };

  return (
    <div>
      <ToastStack toasts={toasts.toasts} onDismiss={toasts.dismiss} />

      <div className="filter-bar">
        <label>
          Class Section
          <select
            value={classSectionId}
            onChange={(e) => setClassSectionId(e.target.value)}
            disabled={loadingSetup}
          >
            <option value="">{loadingSetup ? "Loading…" : "Select…"}</option>
            {classSections.map((cs) => (
              <option key={cs.id} value={cs.id}>
                {cs.label}
              </option>
            ))}
          </select>
        </label>

        <label>
          Subject
          <select
            value={subjectId}
            onChange={(e) => setSubjectId(e.target.value)}
            disabled={loadingSetup}
          >
            <option value="">Select…</option>
            {subjects.map((s) => (
              <option key={s.value} value={s.value}>
                {s.label}
              </option>
            ))}
          </select>
        </label>

        <label>
          Teacher
          <select
            value={teacherId}
            onChange={(e) => setTeacherId(e.target.value)}
            disabled={loadingSetup}
          >
            <option value="">Select…</option>
            {teachers.map((t) => (
              <option key={t.value} value={t.value}>
                {t.label}
              </option>
            ))}
          </select>
        </label>

        <label>
          Date
          <input type="date" value={date} onChange={(e) => setDate(e.target.value)} />
        </label>
      </div>

      <div className="card">
        {!classSectionId ? (
          <div className="empty-state">Pick a class section to see its student roster.</div>
        ) : loadingRoster ? (
          <div className="empty-state">Loading roster…</div>
        ) : roster.length === 0 ? (
          <div className="empty-state">No students enrolled in this class section yet.</div>
        ) : !subjectId ? (
          <div className="empty-state">Pick a subject to mark attendance.</div>
        ) : (
          <>
            <div className="page-header" style={{ padding: "16px 16px 0" }}>
              <div />
              <button type="button" className="btn btn--ghost btn--sm" onClick={markAllPresent}>
                Mark All Present
              </button>
            </div>
            <table className="table">
              <thead>
                <tr>
                  <th>Roll No.</th>
                  <th>Name</th>
                  <th>Status</th>
                  <th>Remarks</th>
                </tr>
              </thead>
              <tbody>
                {roster
                  .slice()
                  .sort((a, b) => a.rollNumber.localeCompare(b.rollNumber, undefined, { numeric: true }))
                  .map((r) => (
                    <tr key={r.id}>
                      <td>{r.rollNumber}</td>
                      <td className="cell-strong">{r.name}</td>
                      <td>
                        <select
                          value={rows[r.id]?.status ?? "PRESENT"}
                          onChange={(e) => setRowStatus(r.id, e.target.value as AttendanceStatus)}
                        >
                          {ATTENDANCE_STATUSES.map((status) => (
                            <option key={status} value={status}>
                              {status}
                            </option>
                          ))}
                        </select>
                      </td>
                      <td>
                        <input
                          value={rows[r.id]?.remarks ?? ""}
                          onChange={(e) => setRowRemarks(r.id, e.target.value)}
                          placeholder="Optional"
                        />
                      </td>
                    </tr>
                  ))}
              </tbody>
            </table>
            <div className="form-actions" style={{ padding: 16 }}>
              <button
                type="button"
                className="btn btn--primary"
                onClick={save}
                disabled={!canSave || saving}
              >
                {saving ? "Saving…" : `Save Attendance (${roster.length})`}
              </button>
            </div>
          </>
        )}
      </div>
    </div>
  );
}
