import { useEffect, useState } from "react";
import { examApi } from "../api/examApi";
import { ApiError } from "../api/client";
import { loadClassRoomOptions, type ReferenceOption } from "../api/directory";
import { Modal } from "../components/Modal";
import { ConfirmDialog } from "../components/ConfirmDialog";
import { StatusBadge } from "../components/StatusBadge";
import { ToastStack } from "../components/Toast";
import { useToasts } from "../hooks/useToasts";
import { EXAM_STATUSES, type Exam, type ExamRequest } from "../types/exam";

const EMPTY_FORM: ExamRequest = {
  name: "",
  academicYear: "",
  classId: "",
  startDate: "",
  endDate: "",
  status: "DRAFT",
  description: "",
};

export function ExamsPage() {
  const [exams, setExams] = useState<Exam[]>([]);
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState<Exam | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState<ExamRequest>(EMPTY_FORM);
  const [deleteTarget, setDeleteTarget] = useState<Exam | null>(null);
  const [saving, setSaving] = useState(false);
  const [classRooms, setClassRooms] = useState<ReferenceOption[]>([]);
  const toasts = useToasts();

  useEffect(() => {
    loadClassRoomOptions()
      .then(setClassRooms)
      .catch((err) =>
        toasts.error(err instanceof ApiError ? err.message : "Failed to load classes"),
      );
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const classRoomName = (id: string) =>
    classRooms.find((c) => c.value === id)?.label ?? id;

  const load = async () => {
    setLoading(true);
    try {
      setExams(await examApi.list());
    } catch (err) {
      toasts.error(err instanceof ApiError ? err.message : "Failed to load exams");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const openCreate = () => {
    setEditing(null);
    setForm(EMPTY_FORM);
    setShowForm(true);
  };

  const openEdit = (exam: Exam) => {
    setEditing(exam);
    setForm({
      name: exam.name,
      academicYear: exam.academicYear,
      classId: exam.classId,
      startDate: exam.startDate,
      endDate: exam.endDate,
      status: exam.status,
      description: exam.description ?? "",
    });
    setShowForm(true);
  };

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    try {
      if (editing) {
        await examApi.update(editing.id, form);
        toasts.success("Exam updated");
      } else {
        await examApi.create(form);
        toasts.success("Exam created");
      }
      setShowForm(false);
      await load();
    } catch (err) {
      toasts.error(err instanceof ApiError ? err.message : "Save failed");
    } finally {
      setSaving(false);
    }
  };

  const confirmDelete = async () => {
    if (!deleteTarget) return;
    try {
      await examApi.remove(deleteTarget.id);
      toasts.success("Exam deleted");
      setDeleteTarget(null);
      await load();
    } catch (err) {
      toasts.error(err instanceof ApiError ? err.message : "Delete failed");
    }
  };

  return (
    <div>
      <ToastStack toasts={toasts.toasts} onDismiss={toasts.dismiss} />

      <div className="page-header">
        <div>
          <h2>Exams</h2>
          <p className="page-subtitle">
            Define exam periods that schedules and results attach to.
          </p>
        </div>
        <button type="button" className="btn btn--primary" onClick={openCreate}>
          + New Exam
        </button>
      </div>

      <div className="card">
        {loading ? (
          <div className="empty-state">Loading exams…</div>
        ) : exams.length === 0 ? (
          <div className="empty-state">
            No exams yet. Create one to get started.
          </div>
        ) : (
          <table className="table">
            <thead>
              <tr>
                <th>Name</th>
                <th>Academic Year</th>
                <th>Class</th>
                <th>Start</th>
                <th>End</th>
                <th>Status</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {exams.map((exam) => (
                <tr key={exam.id}>
                  <td className="cell-strong">{exam.name}</td>
                  <td>{exam.academicYear}</td>
                  <td>{classRoomName(exam.classId)}</td>
                  <td>{exam.startDate}</td>
                  <td>{exam.endDate}</td>
                  <td>
                    <StatusBadge value={exam.status} />
                  </td>
                  <td className="cell-actions">
                    <button
                      type="button"
                      className="btn btn--ghost btn--sm"
                      onClick={() => openEdit(exam)}
                    >
                      Edit
                    </button>
                    <button
                      type="button"
                      className="btn btn--ghost btn--sm btn--danger-text"
                      onClick={() => setDeleteTarget(exam)}
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
          title={editing ? "Edit Exam" : "New Exam"}
          onClose={() => setShowForm(false)}
        >
          <form onSubmit={submit} className="form">
            <div className="form-row">
              <label>
                Name
                <input
                  required
                  maxLength={100}
                  value={form.name}
                  onChange={(e) => setForm({ ...form, name: e.target.value })}
                  placeholder="Mid Term Examination"
                />
              </label>
              <label>
                Academic Year
                <input
                  required
                  value={form.academicYear}
                  onChange={(e) =>
                    setForm({ ...form, academicYear: e.target.value })
                  }
                  placeholder="2025-2026"
                />
              </label>
            </div>

            <label>
              Class
              <select
                required
                value={form.classId}
                onChange={(e) => setForm({ ...form, classId: e.target.value })}
              >
                <option value="">Select…</option>
                {classRooms.map((c) => (
                  <option key={c.value} value={c.value}>
                    {c.label}
                  </option>
                ))}
              </select>
            </label>

            <div className="form-row">
              <label>
                Start Date
                <input
                  required
                  type="date"
                  value={form.startDate}
                  onChange={(e) =>
                    setForm({ ...form, startDate: e.target.value })
                  }
                />
              </label>
              <label>
                End Date
                <input
                  required
                  type="date"
                  value={form.endDate}
                  onChange={(e) => setForm({ ...form, endDate: e.target.value })}
                />
              </label>
            </div>

            <label>
              Status
              <select
                value={form.status}
                onChange={(e) =>
                  setForm({ ...form, status: e.target.value as Exam["status"] })
                }
              >
                {EXAM_STATUSES.map((status) => (
                  <option key={status} value={status}>
                    {status}
                  </option>
                ))}
              </select>
            </label>

            <label>
              Description
              <textarea
                maxLength={500}
                value={form.description}
                onChange={(e) =>
                  setForm({ ...form, description: e.target.value })
                }
                rows={3}
              />
            </label>

            <div className="form-actions">
              <button
                type="button"
                className="btn btn--ghost"
                onClick={() => setShowForm(false)}
              >
                Cancel
              </button>
              <button type="submit" className="btn btn--primary" disabled={saving}>
                {saving ? "Saving…" : "Save Exam"}
              </button>
            </div>
          </form>
        </Modal>
      )}

      {deleteTarget && (
        <ConfirmDialog
          message={`Delete exam "${deleteTarget.name}"? This cannot be undone.`}
          onConfirm={confirmDelete}
          onCancel={() => setDeleteTarget(null)}
        />
      )}
    </div>
  );
}
