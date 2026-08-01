import { useEffect, useState } from "react";
import { examApi } from "../api/examApi";
import { examResultApi } from "../api/examResultApi";
import { ApiError } from "../api/client";
import {
  loadStudentEnrollmentOptions,
  loadSubjectOptions,
  type ReferenceOption,
} from "../api/directory";
import { Modal } from "../components/Modal";
import { ConfirmDialog } from "../components/ConfirmDialog";
import { StatusBadge } from "../components/StatusBadge";
import { ToastStack } from "../components/Toast";
import { useToasts } from "../hooks/useToasts";
import type { Exam } from "../types/exam";
import type { ExamResult, ExamResultRequest } from "../types/examResult";

const emptyForm = (examId: string): ExamResultRequest => ({
  examId,
  studentEnrollmentId: "",
  subjectId: "",
  marksObtained: 0,
  maxMarks: 100,
  absent: false,
});

export function ExamResultsPage() {
  const [exams, setExams] = useState<Exam[]>([]);
  const [selectedExamId, setSelectedExamId] = useState("");
  const [studentFilter, setStudentFilter] = useState("");
  const [results, setResults] = useState<ExamResult[]>([]);
  const [loading, setLoading] = useState(false);
  const [editing, setEditing] = useState<ExamResult | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState<ExamResultRequest>(emptyForm(""));
  const [deleteTarget, setDeleteTarget] = useState<ExamResult | null>(null);
  const [saving, setSaving] = useState(false);
  const [studentEnrollments, setStudentEnrollments] = useState<ReferenceOption[]>([]);
  const [subjects, setSubjects] = useState<ReferenceOption[]>([]);
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

    Promise.all([loadStudentEnrollmentOptions(), loadSubjectOptions()])
      .then(([enrollments, subj]) => {
        setStudentEnrollments(enrollments);
        setSubjects(subj);
      })
      .catch((err) =>
        toasts.error(
          err instanceof ApiError ? err.message : "Failed to load reference lists",
        ),
      );
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const nameOf = (options: ReferenceOption[], id: string) =>
    options.find((o) => o.value === id)?.label ?? id;

  const loadResults = async (examId: string, studentEnrollmentId: string) => {
    if (!examId) {
      setResults([]);
      return;
    }
    setLoading(true);
    try {
      const data = studentEnrollmentId.trim()
        ? await examResultApi.resultCard(examId, studentEnrollmentId.trim())
        : await examResultApi.listByExam(examId);
      setResults(data);
    } catch (err) {
      toasts.error(
        err instanceof ApiError ? err.message : "Failed to load results",
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadResults(selectedExamId, studentFilter);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedExamId]);

  const openCreate = () => {
    setEditing(null);
    setForm(emptyForm(selectedExamId));
    setShowForm(true);
  };

  const openEdit = (result: ExamResult) => {
    setEditing(result);
    setForm({
      examId: result.examId,
      studentEnrollmentId: result.studentEnrollmentId,
      subjectId: result.subjectId,
      marksObtained: result.marksObtained,
      maxMarks: result.maxMarks,
      absent: result.absent,
    });
    setShowForm(true);
  };

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    try {
      if (editing) {
        await examResultApi.update(editing.id, form);
        toasts.success("Result updated");
      } else {
        await examResultApi.create(form);
        toasts.success("Result recorded");
      }
      setShowForm(false);
      await loadResults(selectedExamId, studentFilter);
    } catch (err) {
      toasts.error(err instanceof ApiError ? err.message : "Save failed");
    } finally {
      setSaving(false);
    }
  };

  const confirmDelete = async () => {
    if (!deleteTarget) return;
    try {
      await examResultApi.remove(deleteTarget.id);
      toasts.success("Result deleted");
      setDeleteTarget(null);
      await loadResults(selectedExamId, studentFilter);
    } catch (err) {
      toasts.error(err instanceof ApiError ? err.message : "Delete failed");
    }
  };

  return (
    <div>
      <ToastStack toasts={toasts.toasts} onDismiss={toasts.dismiss} />

      <div className="page-header">
        <div>
          <h2>Exam Results</h2>
          <p className="page-subtitle">
            Marks, grade and pass/fail status per student per subject.
          </p>
        </div>
        <button
          type="button"
          className="btn btn--primary"
          onClick={openCreate}
          disabled={!selectedExamId}
        >
          + New Result
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

        <label>
          Filter by Student (result card)
          <select
            value={studentFilter}
            onChange={(e) => {
              setStudentFilter(e.target.value);
              loadResults(selectedExamId, e.target.value);
            }}
          >
            <option value="">All students</option>
            {studentEnrollments.map((s) => (
              <option key={s.value} value={s.value}>
                {s.label}
              </option>
            ))}
          </select>
        </label>
      </div>

      <div className="card">
        {loading ? (
          <div className="empty-state">Loading results…</div>
        ) : results.length === 0 ? (
          <div className="empty-state">No results recorded for this view yet.</div>
        ) : (
          <table className="table">
            <thead>
              <tr>
                <th>Student Enrollment</th>
                <th>Subject</th>
                <th>Marks</th>
                <th>%</th>
                <th>Grade</th>
                <th>Status</th>
                <th>Absent</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {results.map((result) => (
                <tr key={result.id}>
                  <td>{nameOf(studentEnrollments, result.studentEnrollmentId)}</td>
                  <td>{nameOf(subjects, result.subjectId)}</td>
                  <td>
                    {result.marksObtained} / {result.maxMarks}
                  </td>
                  <td>{result.percentage.toFixed(1)}%</td>
                  <td>
                    <StatusBadge value={result.grade} />
                  </td>
                  <td>
                    <StatusBadge value={result.resultStatus} />
                  </td>
                  <td>{result.absent ? "Yes" : "—"}</td>
                  <td className="cell-actions">
                    <button
                      type="button"
                      className="btn btn--ghost btn--sm"
                      onClick={() => openEdit(result)}
                    >
                      Edit
                    </button>
                    <button
                      type="button"
                      className="btn btn--ghost btn--sm btn--danger-text"
                      onClick={() => setDeleteTarget(result)}
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
          title={editing ? "Edit Result" : "New Result"}
          onClose={() => setShowForm(false)}
        >
          <form onSubmit={submit} className="form">
            <label>
              Student Enrollment
              <select
                required
                value={form.studentEnrollmentId}
                onChange={(e) =>
                  setForm({ ...form, studentEnrollmentId: e.target.value })
                }
              >
                <option value="">Select…</option>
                {studentEnrollments.map((s) => (
                  <option key={s.value} value={s.value}>
                    {s.label}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Subject
              <select
                required
                value={form.subjectId}
                onChange={(e) => setForm({ ...form, subjectId: e.target.value })}
              >
                <option value="">Select…</option>
                {subjects.map((s) => (
                  <option key={s.value} value={s.value}>
                    {s.label}
                  </option>
                ))}
              </select>
            </label>

            <label className="checkbox-row">
              <input
                type="checkbox"
                checked={form.absent}
                onChange={(e) =>
                  setForm({
                    ...form,
                    absent: e.target.checked,
                    marksObtained: e.target.checked ? 0 : form.marksObtained,
                  })
                }
              />
              Student was absent (auto-fails, marks ignored)
            </label>

            <div className="form-row">
              <label>
                Marks Obtained
                <input
                  required
                  type="number"
                  min={0}
                  step="0.5"
                  disabled={form.absent}
                  value={form.marksObtained}
                  onChange={(e) =>
                    setForm({ ...form, marksObtained: Number(e.target.value) })
                  }
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

            <p className="form-hint">
              Grade and pass/fail status are computed automatically from marks —
              you don't set them directly.
            </p>

            <div className="form-actions">
              <button
                type="button"
                className="btn btn--ghost"
                onClick={() => setShowForm(false)}
              >
                Cancel
              </button>
              <button type="submit" className="btn btn--primary" disabled={saving}>
                {saving ? "Saving…" : "Save Result"}
              </button>
            </div>
          </form>
        </Modal>
      )}

      {deleteTarget && (
        <ConfirmDialog
          message="Delete this exam result? This cannot be undone."
          onConfirm={confirmDelete}
          onCancel={() => setDeleteTarget(null)}
        />
      )}
    </div>
  );
}
