export interface TechnicalTask {
  id: number;
  description: string;
  status: 'TO_DO' | 'IN_PROGRESS' | 'DONE'; // Example statuses
  jiraTicketId?: string; // Optional, if it becomes a sub-task in Jira
}