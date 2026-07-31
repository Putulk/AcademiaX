import { useState } from "react";
import { userProfileApi } from "../api/userProfileApi";
import { CrudPage, type CrudField, type CrudColumn } from "../components/CrudPage";
import { RoleAssignModal } from "../components/RoleAssignModal";
import { StatusBadge } from "../components/StatusBadge";
import { useAuth } from "../auth/AuthContext";
import { GENDERS, type UserProfile, type UserProfileRequest } from "../types/userProfile";

const columns: CrudColumn<UserProfile>[] = [
  { key: "firstName", label: "First Name" },
  { key: "lastName", label: "Last Name" },
  { key: "userId", label: "User ID", mono: true },
  { key: "phone", label: "Phone" },
  {
    key: "active",
    label: "Status",
    render: (item) => <StatusBadge value={item.active ? "ACTIVE" : "INACTIVE"} />,
  },
];

const fields: CrudField<UserProfileRequest>[] = [
  { key: "userId", label: "User ID (UUID, from auth-service)", type: "text", required: true },
  { key: "firstName", label: "First Name", type: "text", required: true },
  { key: "lastName", label: "Last Name", type: "text", required: true },
  { key: "phone", label: "Phone", type: "text" },
  { key: "gender", label: "Gender", type: "select", options: GENDERS },
  { key: "dateOfBirth", label: "Date of Birth", type: "date" },
  { key: "address", label: "Address", type: "text" },
  { key: "city", label: "City", type: "text" },
  { key: "state", label: "State", type: "text" },
  { key: "country", label: "Country", type: "text" },
  { key: "active", label: "Active", type: "checkbox" },
];

const emptyForm: UserProfileRequest = {
  userId: "",
  firstName: "",
  lastName: "",
  phone: "",
  dateOfBirth: "",
  address: "",
  city: "",
  state: "",
  country: "",
  active: true,
};

export function UserProfilesPage() {
  const auth = useAuth();
  const [assigning, setAssigning] = useState<UserProfile | null>(null);

  return (
    <div>
      <CrudPage
        title="User Profiles"
        subtitle="Human profile data (name, contact, DOB) for a login identity created in auth-service."
        api={userProfileApi}
        columns={columns}
        fields={fields}
        emptyForm={emptyForm}
        toRequest={(item) => ({
          userId: item.userId,
          firstName: item.firstName,
          lastName: item.lastName,
          phone: item.phone ?? "",
          gender: item.gender,
          dateOfBirth: item.dateOfBirth ?? "",
          address: item.address ?? "",
          city: item.city ?? "",
          state: item.state ?? "",
          country: item.country ?? "",
          active: item.active,
        })}
        itemLabel={(item) => `${item.firstName} ${item.lastName}`}
        extraActions={
          auth.isAdmin
            ? (item) => (
                <button
                  type="button"
                  className="btn btn--ghost btn--sm"
                  onClick={() => setAssigning(item)}
                >
                  Assign Role
                </button>
              )
            : undefined
        }
      />

      {assigning && (
        <RoleAssignModal
          userId={assigning.userId}
          displayName={`${assigning.firstName} ${assigning.lastName}`}
          onClose={() => setAssigning(null)}
        />
      )}
    </div>
  );
}
