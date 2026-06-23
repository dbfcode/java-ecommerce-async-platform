import { Link } from "react-router-dom";

interface AuthFooterLinkProps {
  question: string;
  actionLabel: string;
  to: string;
}

export function AuthFooterLink({ question, actionLabel, to }: AuthFooterLinkProps) {
  return (
    <p className="text-center text-sm text-textSecondary mt-3">
      {question}{" "}
      <Link to={to} className="font-medium text-text underline-offset-4 hover:underline">
        {actionLabel}
      </Link>
    </p>
  );
}