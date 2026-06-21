interface SubmitButtonProps {
  label: string;
  loadingLabel?: string;
  isLoading?: boolean;
  disabled?: boolean;
}

export function SubmitButton({
  label,
  loadingLabel = "Carregando...",
  isLoading = false,
  disabled = false,
}: SubmitButtonProps) {
  return (
    <button
      type="submit"
      disabled={disabled || isLoading}
      className="h-11 w-full rounded-lg bg-text text-sm font-medium text-surface transition-opacity hover:opacity-90 disabled:cursor-not-allowed disabled:opacity-40"
    >
      {isLoading ? loadingLabel : label}
    </button>
  );
}