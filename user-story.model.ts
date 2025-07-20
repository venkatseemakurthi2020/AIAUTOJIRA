import { TechnicalTask } from './technical-task.model';

export interface UserStory {
  id: number;
  title: string; // "As a [who], I want [what], so that [why]."
  who: string;
  what: string;
  why: string;
  status: 'PENDING_REVIEW' | 'APPROVED' | 'REJECTED' | 'JIRA_CREATED';
  generatedDate: Date;
  approvedByUserId?: number; // Optional, if approved by a user
  jiraTicketId?: string; // Optional, if a Jira ticket is created
  tasks: TechnicalTask[];
}