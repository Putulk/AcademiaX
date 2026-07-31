import { useEffect, useState } from "react";
import { examApi } from "../api/examApi";
import { examScheduleApi } from "../api/examScheduleApi";
import { ApiError } from "../api/client";
import { Modal } from "../components/Modal";
import { ConfirmDialog } from "../components/ConfirmDialog";
import { ToastStack } from "../components/Toast";
import { useToasts } from "../hooks/useToasts";
import type { Exam } from "../types/exam";
import type { ExamSchedule, ExamScheduleRequest } from "../types/examSchedule";

const emptyForm = (examId: string): ExamScheduleRequest => ({
  examId,
  subjectId: "",
  teacherId: "",
  classId: "",
  sectionId: "",
  examDate: "",
  startTime: "",
  endTime: "",
  roomNumber: "",
  maxMarks: 100,
});

export function ExamSchedulesPage() {
  const [exams, setExams] = useState<Exam[]>([]);
  const [selectedExamId, setSelectedExamId] = useState("");
  const [schedules, setSchedules] = useState<ExamSchedule[]>([]);
  const [loading, setLoading] = useState(false);
  const [editing, setEditing] = useState<ExamSchedule | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState<ExamScheduleRequest>(emptyForm(""));
  const [deleteTarget, setDeleteTarget] = useState<ExamSchedule | null>(null);
  const [saving, setSaving] = useState(false);
  const toasts = useToasts();

  useEffect(() => {
    examApi
      .list()
      .then((data) => {
        setExams(data);
        if (data.length > 0) setSelectedExamId(data[0].id);
      })
      .catch((err) =>
        toasts.error(err instanceof ApiError ? err.message : "Failed to load exams"),
      );
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const loadSchedules = async (examId: string) => {
    if (!examId) {
      setSchedules([]);
      return;
    }
    setLoading(true);
    try {
      setSchedules(await examScheduleApi.listByExam(examId));
    } catch (err) {
      toasts.error(
        err instanceof ApiError ? err.message : "Failed to load schedules",
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadSchedules(selectedExamId);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedExamId]);

  const openCreate = () => {
    setEditing(null);
    setForm(emptyForm(selectedExamId));
    setShowForm(true);
  };

  const openEdit = (schedule: ExamSchedule) => {
    setEditing(schedule);
    setForm({
      examId: schedule.examId,
      subjectId: schedule.subjectId,
      teacherId: schedule.teacherId,
      classId: schedule.classId,
      sectionId: schedule.sectionId,
      examDate: schedule.examDate,
      startTime: schedule.startTime,
      endTime: schedule.endTime,
      roomNumber: schedule.roomNumber ?? "",
      maxMarks: schedule.maxMarks,
    });
    setShowForm(true);
  };

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    try {
      if (editing) {
        await examScheduleApi.update(editing.id, form);
        toasts.success("Schedule updated");
      } else {
        await examScheduleApi.create(form);
        toasts.success("Schedule created");
      }
      setShowForm(false);
      await loadSchedules(selectedExamId);
    } catch (err) {
      toasts.error(err instanceof ApiError ? err.message : "Save failed");
    } finally {
      setSaving(false);
    }
  };

  const confirmDelete = async () => {
    if (!deleteTarget) return;
    try {
      await examScheduleApi.remove(deleteTarget.id);
      toasts.success("Schedule deleted");
      setDeleteTarget(null);
      await loadSchedules(selectedExamId);
    } catch (err) {
      toasts.error(err instanceof ApiError ? err.message : "Delete failed");
    }
  };

  return (
    <div>
      <ToastStack toasts={toasts.toasts} onDismiss={toasts.dismiss} />

      <div className="page-header">
        <div>
          <h2>Exam Schedules</h2>
          <p className="page-subtitle">
            Subject-wise date, time and room allocation for an exam.
          </p>
        </div>
        <button
          type="button"
          className="btn btn--primary"
          onClick={openCreate}
          disabled={!selectedExamId}
        >
          + New Schedule
        </button>
      </div>

      <div className="filter-bar">
        <label>
          Exam
          <select
            value={selectedExamId}
            onChange={(e) => setSelectedExamId(e.target.value)}
          >
            {exams.length === 0 && <option value="">No exams yet</option>}
            {exams.map((exam) => (
              <option key={exam.id} value={exam.id}>
                {exam.name} ({exam.academicYear})
              </option>
            ))}
          </select>
        </label>
      </div>

      <div className="card">
        {loading ? (
          <div className="empty-state">Loading schedules…</div>
        ) : schedules.length === 0 ? (
          <div className="empty-state">
            No schedules for this exam yet.
          </div>
        ) : (
          <table className="table">
            <thead>
              <tr>
                <th>Subject ID</th>
                <th>Teacher ID</th>
                <th>Section ID</th>
                <th>Date</th>
                <th>Time</th>
                <th>Room</th>
                <th>Max Marks</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {schedules.map((schedule) => (
                <tr key={schedule.id}>
                  <td className="cell-mono">{schedule.subjectId}</td>
                  <td className="cell-mono">{schedule.teacherId}</td>
                  <td className="cell-mono">{schedule.sectionId}</td>
                  <td>{schedule.examDate}</td>
                  <td>
                    {schedule.startTime}–{schedule.endTime}
                  </td>
                  <td>{schedule.roomNumber || "—"}</td>
                  <td>{schedule.maxMarks}</td>
                  <td className="cell-actions">
                    <button
                      type="button"
                      className="btn btn--ghost btn--sm"
                      onClick={() => openEdit(schedule)}
                    >
                      Edit
                    </button>
                    <button
                      type="button"
                      className="btn btn--ghost btn--sm btn--danger-text"
                      onClick={() => setDeleteTarget(schedule)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {showForm && (
        <Modal
          title={editing ? "Edit Schedule" : "New Schedule"}
          onClose={() => setShowForm(false)}
        >
          <form onSubmit={submit} className="form">
            <label>
              Subject ID (UUID)
              <input
                required
                value={form.subjectId}
                onChange={(e) => setForm({ ...form, subjectId: e.target.value })}
              />
            </label>
            <label>
              Teacher ID (UUID)
              <input
                required
                value={form.teacherId}
                onChange={(e) => setForm({ ...form, teacherId: e.target.value })}
              />
            </label>
            <div className="form-row">
              <label>
                Class ID (UUID)
                <input
                  required
                  value={form.classId}
                  onChange={(e) => setForm({ ...form, classId: e.target.value })}
                />
              </label>
              <label>
                Section ID (UUID)
                <input
                  required
                  value={form.sectionId}
                  onChange={(e) => setForm({ ...form, sectionId: e.target.value })}
                />
              </label>
            </div>

            <label>
              Exam Date
              <input
                required
                type="date"
                value={form.examDate}
                onChange={(e) => setForm({ ...form, examDate: e.target.value })}
              />
            </label>

            <div className="form-row">
              <label>
                Start Time
                <input
                  required
                  type="time"
                  value={form.startTime}
                  onChange={(e) => setForm({ ...form, startTime: e.target.value })}
                />
              </label>
              <label>
                End Time
                <input
                  required
                  type="time"
                  value={form.endTime}
                  onChange={(e) => setForm({ ...form, endTime: e.target.value })}
                />
              </label>
            </div>

            <div className="form-row">
              <label>
                Room Number
                <input
                  value={form.roomNumber}
                  onChange={(e) =>
                    setForm({ ...form, roomNumber: e.target.value })
                  }
                  placeholder="Room 204"
                />
              </label>
              <label>
                Max Marks
                <input
                  required
                  type="number"
                  min={1}
                  value={form.maxMarks}
                  onChange={(e) =>
                    setForm({ ...form, maxMarks: Number(e.target.value) })
                  }
                />
              </label>
            </div>

            <div className="form-actions">
              <button
                type="button"
                className="btn btn--ghost"
                onClick={() => setShowForm(false)}
              >
                Cancel
              </button>
              <button type="submit" className="btn btn--primary" disabled={saving}>
                {saving ? "Saving…" : "Save Schedule"}
              </button>
            </div>
          </form>
        </Modal>
      )}

      {deleteTarget && (
        <ConfirmDialog
          message="Delete this exam schedule? This cannot be undone."
          onConfirm={confirmDelete}
          onCancel={() => setDeleteTarget(null)}
        />
      )}
    </div>
  );
}
